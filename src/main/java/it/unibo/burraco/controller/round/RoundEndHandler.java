package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.components.sound.SoundView;
import it.unibo.burraco.view.scenes.ScoreView;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.controller.score.ScoreController.ViewProvider;

import javax.swing.SwingUtilities;

/**
 * Handles the end-of-round sequence: sound, score display, match termination check.
 * Extracted from ScoreController to respect Single Responsibility Principle.
 */
public final class RoundEndHandler {

    private final Score score;
    private final Player player1;
    private final Player player2;
    private final String nameP1;
    private final String nameP2;
    private final TableView tableView;
    private final SoundView soundView;
    private final int targetScore;
    private final ViewProvider viewProvider;
    private RoundController roundFactory;

    public RoundEndHandler(
            final Score score,
            final Player player1,
            final Player player2,
            final String nameP1,
            final String nameP2,
            final TableView tableView,
            final SoundView soundView,
            final int targetScore,
            final ViewProvider viewProvider) {
        this.score = score;
        this.player1 = player1;
        this.player2 = player2;
        this.nameP1 = nameP1;
        this.nameP2 = nameP2;
        this.tableView = tableView;
        this.soundView = soundView;
        this.targetScore = targetScore;
        this.viewProvider = viewProvider;
    }

    public void setRoundFactory(final RoundController factory) {
        this.roundFactory = factory;
    }

    /**
     * Executes the full end-of-round sequence.
     */
    public void handle() {
        final boolean matchOver =
            player1.getMatchTotalScore() >= targetScore
            || player2.getMatchTotalScore() >= targetScore;

        if (matchOver) {
            new Thread(() -> {
                soundView.playVictorySound();
                SwingUtilities.invokeLater(() -> showScoreView(true));
            }).start();
        } else {
            soundView.playRoundEndSound();
            SwingUtilities.invokeLater(() -> showScoreView(false));
        }
    }

    private void showScoreView(final boolean matchOver) {
        tableView.getPlayer1HandView().updateHand(player1.getHand());
        tableView.getPlayer2HandView().updateHand(player2.getHand());

        final ScoreView view = viewProvider.create(
            player1, player2, nameP1, nameP2,
            targetScore, score, tableView, matchOver);

        view.setOnNextAction(() -> {
            view.close();
            roundFactory.processNewRound();
            tableView.refreshTurnLabel(true);
        });
        view.display();
    }
}