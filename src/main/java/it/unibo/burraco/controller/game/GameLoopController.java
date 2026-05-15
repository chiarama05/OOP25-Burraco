package it.unibo.burraco.controller.game;

import it.unibo.burraco.controller.display.CombinationDisplaySorter;
import it.unibo.burraco.controller.display.GameState;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.components.sound.SoundView;
import it.unibo.burraco.view.table.BurracoView;
import it.unibo.burraco.view.table.MoveError;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Main controller managing the game cycle.
 * Extracts all data from the model before crossing into the view layer —
 * the view never receives Player or MoveResult directly.
 */
public final class GameLoopController implements GameController {

    private final GameModel model;
    private final BurracoView view;
    private final SoundView sound;
    private final PotManager potManager;
    private final ScoreController scoreController;
    private final CombinationDisplaySorter displaySorter = new CombinationDisplaySorter();

    public GameLoopController(final GameModel model,
                              final BurracoView view,
                              final SoundView sound,
                              final PotManager potManager,
                              final ScoreController scoreController) {
        this.model = model;
        this.view = view;
        this.sound = sound;
        this.potManager = potManager;
        this.scoreController = scoreController;
    }

    @Override
    public void start() {
        final Thread gameThread = new Thread(this::loop);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void loop() {
        boolean isStartOfTurn = true;

        while (!model.isGameOver()) {
            final Player current = model.getCurrentPlayer();
            final boolean isP1 = model.isPlayer1(current);

            if (isStartOfTurn) {
                final String name = current.getName();
                final var hand = current.getHand();
                try {
                    SwingUtilities.invokeAndWait(() -> view.wakeUp(name, isP1, hand));
                } catch (InvocationTargetException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Move move = waitForMove();
            MoveResult validation = model.validateMove(move);
            while (!validation.isValid()) {
                final MoveError err = toMoveError(validation.getStatus());
                SwingUtilities.invokeLater(() -> view.showMoveError(err));
                move = waitForMove();
                validation = model.validateMove(move);
            }

            final MoveResult result = model.applyMove(move);
            final Move finalMove = move;

            try {
                SwingUtilities.invokeAndWait(() -> handleResult(result, finalMove));
            } catch (InvocationTargetException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (finalMove.getType() == Move.Type.DISCARD && !model.isGameOver()) {
                model.nextTurn();
                isStartOfTurn = true;
            } else {
                isStartOfTurn = false;
            }
        }
        scoreController.onRoundEnd();
    }

    private void handleResult(final MoveResult result, final Move move) {
        switch (result.getStatus()) {
            case SUCCESS_BURRACO -> sound.playBurracoSound();
            case SUCCESS_TAKE_POT -> {
                final boolean isDiscard = move.getType() == Move.Type.DISCARD;
                potManager.handlePot(isDiscard);
            }
            case ROUND_WON -> sound.playRoundEndSound();
            case DECK_EMPTY -> sound.playRoundEndSound();
            default -> { }
        }
        view.refresh(buildGameState());
    }

    private GameState buildGameState() {
    final Player current = model.getCurrentPlayer();
    final boolean isP1 = model.isPlayer1(current);

    final List<List<Card>> p1Sorted = model.getPlayer1().getCombinations().stream()
            .map(c -> displaySorter.sortForDisplay(new ArrayList<>(c)))
            .collect(Collectors.toList());

    final List<List<Card>> p2Sorted = model.getPlayer2().getCombinations().stream()
            .map(c -> displaySorter.sortForDisplay(new ArrayList<>(c)))
            .collect(Collectors.toList());

    return new GameState(
        p1Sorted,
        p2Sorted,
        isP1,
        current.getHand(),
        model.getDiscardPile().getCards()
    );
}

    private Move waitForMove() {
        final CompletableFuture<Move> future = new CompletableFuture<>();
        SwingUtilities.invokeLater(() -> view.setMoveFuture(future));
        try {
            return future.get();
        } catch (final InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Game loop interrupted", e);
        }
    }

    /**
     * The only place where model enum and view enum meet — in the controller,
     * which is allowed to know both sides.
     */
    private static MoveError toMoveError(final MoveResult.Status status) {
        return switch (status) {
            case ALREADY_DRAWN       -> MoveError.ALREADY_DRAWN;
            case NOT_DRAWN           -> MoveError.NOT_DRAWN;
            case NO_CARDS_SELECTED   -> MoveError.NO_CARDS_SELECTED;
            case INVALID_COMBINATION -> MoveError.INVALID_COMBINATION;
            case WOULD_GET_STUCK     -> MoveError.WOULD_GET_STUCK;
            case WRONG_PLAYER        -> MoveError.WRONG_PLAYER;
            default                  -> MoveError.UNKNOWN;
        };
    }
}
