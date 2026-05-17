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
import it.unibo.burraco.view.table.ViewAction;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Main controller managing the game cycle.
 * Receives {@link ViewAction} from the View (view-layer DTO, no model types),
 * translates them into {@link Move} via {@link #toMove(ViewAction)} — the only
 * place where the two layers meet — and extracts all data from the Model before
 * crossing into the view layer, so the view never receives {@code Player} or
 * {@code MoveResult} directly.
 */
public final class GameLoopController implements GameController {

    private final GameModel model;
    private final BurracoView view;
    private final SoundView sound;
    private final PotManager potManager;
    private final ScoreController scoreController;
    private final CombinationDisplaySorter displaySorter = new CombinationDisplaySorter();

    /**
     * Constructs a GameLoopController wiring the model, view and supporting services.
     *
     * @param model           the game model facade
     * @param view            the main table view
     * @param sound           the sound feedback component
     * @param potManager      the handler for pot acquisition events
     * @param scoreController the controller managing end-of-round scoring
     */
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
            ViewAction action = waitForAction();
            Move move = toMove(action);
            MoveResult validation = model.validateMove(move);
            while (!validation.isValid()) {
                final MoveError err = toMoveError(validation.getStatus());
                SwingUtilities.invokeLater(() -> view.showMoveError(err));
                action = waitForAction();
                move = toMove(action);
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

    /**
     * Builds an immutable snapshot of the current game state for the view.
     *
     * @return a {@link GameState} reflecting the current table, hand and discard pile
     */
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
                model.getDiscardPile().getCards());
    }

    /**
     * Waits for the view to signal a player action via the injected future.
     */
    private ViewAction waitForAction() {
        final CompletableFuture<ViewAction> future = new CompletableFuture<>();
        SwingUtilities.invokeLater(() -> view.setActionFuture(future));
        try {
            return future.get();
        } catch (final InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Game loop interrupted", e);
        }
    }

    /**
     * Translates a view-layer {@link ViewAction} into a model-layer {@link Move}.
     * This is the single point where the two layers meet in the controller.
     *
     * @param action the action expressed by the view
     * @return the corresponding Move for the model
     */
    private static Move toMove(final ViewAction action) {
        return switch (action.getType()) {
            case DRAW_DECK ->
                new Move(Move.Type.DRAW_DECK, Collections.emptyList());
            case DRAW_DISCARD ->
                new Move(Move.Type.DRAW_DISCARD, Collections.emptyList());
            case PUT_COMBINATION ->
                new Move(Move.Type.PUT_COMBINATION, action.getSelectedCards());
            case ATTACH ->
                new Move(Move.Type.ATTACH,
                         action.getSelectedCards(),
                         action.getTargetCombination());
            case DISCARD ->
                new Move(Move.Type.DISCARD, action.getSelectedCards());
        };
    }

    /**
     * Maps a model {@link MoveResult.Status} to a view-layer {@link MoveError}.
     * The only place where model enum and view enum meet — in the controller.
     *
     * @param status the model-layer status to translate
     * @return the corresponding view-layer {@link MoveError}
     */
    private static MoveError toMoveError(final MoveResult.Status status) {
        return switch (status) {
            case ALREADY_DRAWN -> MoveError.ALREADY_DRAWN;
            case NOT_DRAWN -> MoveError.NOT_DRAWN;
            case NO_CARDS_SELECTED -> MoveError.NO_CARDS_SELECTED;
            case INVALID_COMBINATION -> MoveError.INVALID_COMBINATION;
            case WOULD_GET_STUCK -> MoveError.WOULD_GET_STUCK;
            case WRONG_PLAYER -> MoveError.WRONG_PLAYER;
            default -> MoveError.UNKNOWN;
        };
    }
}
