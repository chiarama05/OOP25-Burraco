package it.unibo.burraco.controller.game;

import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.BurracoView;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class GameLoopController {

    private final GameModel model;
    private final BurracoView view;
    private final SoundController sound;
    private final PotManager potManager;
    private final ScoreController scoreController;

    public GameLoopController(final GameModel model,
                               final BurracoView view,
                               final SoundController sound,
                               final PotManager potManager,
                               final ScoreController scoreController) {
        this.model = model;
        this.view = view;
        this.sound = sound;
        this.potManager = potManager;
        this.scoreController = scoreController;
    }

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
                SwingUtilities.invokeLater(() -> view.wakeUp(current, isP1));
                try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
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
            } else if (result.getStatus() == MoveResult.Status.SUCCESS_TAKE_POT) {
                isStartOfTurn = false;
            } else {
                isStartOfTurn = false;
            }
        }

        final Player winner = model.getWinner();
        SwingUtilities.invokeLater(() -> view.showWinner(winner));
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
        default -> { }
    }
    view.refresh(model);
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
}