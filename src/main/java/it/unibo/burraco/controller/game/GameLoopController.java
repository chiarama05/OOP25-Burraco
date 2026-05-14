package it.unibo.burraco.controller.game;

import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.GameState;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.BurracoView;
import it.unibo.burraco.view.components.sound.SoundView;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Main controller that manages the game cycle of a Burraco match.
 * It runs the game logic in a dedicated background thread to keep the 
 * UI responsive, orchestrating the interaction between the model, the view, 
 * and sub-controllers like the score and pot managers.
 */
public final class GameLoopController {

    private final GameModel model;
    private final BurracoView view;
    private final SoundView sound;
    private final PotManager potManager;
    private final ScoreController scoreController;

    /**
     * Creates a new GameLoopController with the required components.
     * 
     * @param model           the game model containing business logic and state
     * @param view            the main view interface for user interaction
     * @param sound           the sound system for audio feedback
     * @param potManager      the controller responsible for "pozzetti" management
     * @param scoreController the controller responsible for end-of-round scoring
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

    /**
     * Starts the game loop in a new daemon thread.
     */
    public void start() {
        final Thread gameThread = new Thread(this::loop);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Core game loop logic. Executes turns sequentially, handles move validation,
     * updates the model, and synchronizes with the Event Dispatch Thread (EDT) 
     * for view updates until the game is over.
     */
    private void loop() {
        boolean isStartOfTurn = true;

        while (!model.isGameOver()) {
            final Player current = model.getCurrentPlayer();
            final boolean isP1 = model.isPlayer1(current);

            if (isStartOfTurn) {
                try {
                    SwingUtilities.invokeAndWait(() -> view.wakeUp(current, isP1));
                } catch (InvocationTargetException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Move move = waitForMove();
            MoveResult validation = model.validateMove(move);
            while (!validation.isValid()) {
                final MoveResult err = validation;
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

    /**
     * Processes the result of a move, triggering sounds, pot management, 
     * and UI refreshes based on the status.
     * 
     * @param result the result of the applied move
     * @param move   the move that was executed
     */
    private void handleResult(final MoveResult result, final Move move) {
        switch (result.getStatus()) {
            case SUCCESS_BURRACO -> sound.playBurracoSound();
            case SUCCESS_TAKE_POT -> {
                final boolean isDiscard = move.getType() == Move.Type.DISCARD;
                potManager.handlePot(isDiscard);
            }
            case ROUND_WON -> sound.playRoundEndSound();
            default -> { }
        }
        view.refresh(buildGameState());
    }

    /**
     * Creates a snapshot of the current game status to update the view.
     * 
     * @return a {@link GameState} object containing data for the View
     */
    private GameState buildGameState() {
        final Player current = model.getCurrentPlayer();
        final boolean isP1 = model.isPlayer1(current);
        return new GameState(
            model.getPlayer1().getCombinations(),
            model.getPlayer2().getCombinations(),
            isP1,
            current.getHand(),
            model.getDiscardPile().getCards()
        );
    }

    /**
     * Pauses the game thread and waits for a move to be provided by the View 
     * via a {@link CompletableFuture}.
     * 
     * @return the {@link Move} selected by the player
     * @throws IllegalStateException if the wait is interrupted
     */
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
}
