package it.unibo.burraco.controller.score;

import java.util.function.Consumer;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.round.ResetManagerImpl;
import it.unibo.burraco.controller.round.RoundController;
import it.unibo.burraco.controller.round.RoundControllerImpl;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.score.ScoreView;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.distribution.InitialDistributionView;

/**
 * Controller responsible for managing the end-of-round logic, scoring calculations,
 * and match termination. It acts as an orchestrator between the scoring model,
 * the game state, and the score visualization.
 */
public final class ScoreController {

    private final Score score;
    private final Player player1;
    private final Player player2;
    private final String nameP1;
    private final String nameP2;
    private final TableView tableView;
    private final GameController gameController;
    private final int targetScore;
    private final SoundController soundController;
    private final InitialDistributionView distributionView;
    private final ViewProvider viewProvider;

    /**
     * Constructs the ScoreController with all required dependencies.
     *
     * @param score            the scoring model
     * @param player1          the first player
     * @param player2          the second player
     * @param nameP1           the display name of Player 1
     * @param nameP2           the display name of Player 2
     * @param tableView        the main table view, used by the score screen to navigate back
     * @param gameController   the game controller, used to reset state for a new round
     * @param soundController  the sound controller for victory/round-end audio
     * @param targetScore      the cumulative score threshold that ends the match
     * @param distributionView the distribution view, re-used when starting a new round
     * @param uiThreadRunner   a consumer that schedules tasks on the UI thread (e.g. {@code SwingUtilities::invokeLater})
     * @param viewProvider     a factory for creating the {@link ScoreView}
     */
    public ScoreController(
        final Score score,
        final Player player1,
        final Player player2,
        final String nameP1,
        final String nameP2,
        final TableView tableView,
        final GameController gameController,
        final SoundController soundController,
        final int targetScore,
        final InitialDistributionView distributionView,
        final Consumer<Runnable> uiThreadRunner,
        final ViewProvider viewProvider
    ) {
        this.score = score;
        this.player1 = player1;
        this.player2 = player2;
        this.nameP1 = nameP1;
        this.nameP2 = nameP2;
        this.tableView = tableView;
        this.gameController = gameController;
        this.soundController = soundController;
        this.targetScore = targetScore;
        this.distributionView = distributionView;
        this.viewProvider = viewProvider;
    }

    /**
     * Triggers the end-of-round procedures: calculates points, updates match totals,
     * checks for victory, and triggers audio-visual feedback.
     */
    public void onRoundEnd() {

        // Calculate points earned in the current round
        final int roundS1 = score.calculateFinalScore(player1);
        final int roundS2 = score.calculateFinalScore(player2);

        // Update the total match score for both players
        this.player1.addPointsToMatch(roundS1);
        this.player2.addPointsToMatch(roundS2);

        // Check if any player has reached or exceeded the target match score
        final boolean matchOver = this.player1.getMatchTotalScore() >= this.targetScore 
        || this.player2.getMatchTotalScore() >= this.targetScore;

        if (matchOver) {
            final Thread t = new Thread(() -> {
                this.soundController.playVictorySound();
                this.showScoreView(true);
            });

            t.setDaemon(false);
            t.start();

        } else {
            this.soundController.playRoundEndSound();
            this.showScoreView(matchOver);
        }
    }

    /**
     * Initializes and displays the score view.
     * Sets up the callback for the "Next" action to reset the game for a new round.
     *
     * @param matchOver true if the entire match has concluded.
     */
    private void showScoreView(final boolean matchOver) {
        final ScoreView view = this.viewProvider.create(
                this.player1, this.player2, this.nameP1, this.nameP2,
                this.targetScore, this.score, this.tableView, matchOver
        );

        view.setOnNextAction(() -> {
            view.close();
            this.gameController.getModel().getTurn().resetForNewRound();

            final RoundController roundController = new RoundControllerImpl(
                this.tableView, new ResetManagerImpl(), this.player1, this.player2,
                this.gameController,
                new InitialDistributionController(new DistributionManagerImpl()),
                this.distributionView
            );

            roundController.processNewRound();
            this.tableView.refreshTurnLabel(true);
        });
        view.display();
    }

    /**
     * Interface acting as a Factory for the ScoreView.
     * This abstraction allows for easier testing by enabling the injection
     * of mock views instead of real GUI components.
     */
    @FunctionalInterface
    public interface ViewProvider {

        /**
         * Creates a ScoreView.
         *
         * @param p1 player 1
         * @param p2 player 2
         * @param n1 name 1
         * @param n2 name 2
         * @param target target score
         * @param s score model
         * @param tv table view
         * @param over match over flag
         * @return a new ScoreView
         */
        ScoreView create(Player p1, Player p2, String n1, String n2,
            int target, Score s, TableView tv, boolean over);
    }
}
