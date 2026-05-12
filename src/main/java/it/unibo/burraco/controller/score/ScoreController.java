package it.unibo.burraco.controller.score;

import java.util.function.Consumer;
import java.util.function.Supplier;
import it.unibo.burraco.controller.round.RoundController;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.GameModel;
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
    private final int targetScore;
    private final SoundController soundController;
    private final ViewProvider viewProvider;
    private Supplier<RoundController> roundFactory;
    private final GameModel model;

    public ScoreController(
        final Score score,
        final Player player1,
        final Player player2,
        final String nameP1,
        final String nameP2,
        final TableView tableView,
        final GameModel model,
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
        this.soundController = soundController;
        this.targetScore = targetScore;
        this.viewProvider = viewProvider;
        this.model = model;
    }

    public void setRoundFactory(final Supplier<RoundController> factory) {
        this.roundFactory = factory;
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
        this.tableView.getPlayer1HandView().updateHand(this.player1.getHand());
        this.tableView.getPlayer2HandView().updateHand(this.player2.getHand());
        
        final ScoreView view = this.viewProvider.create(
                this.player1, this.player2, this.nameP1, this.nameP2,
                this.targetScore, this.score, this.tableView, matchOver
        );

        view.setOnNextAction(() -> {
            view.close();
            this.model.getTurn().resetForNewRound();
            this.roundFactory.get().processNewRound();
            tableView.refreshTurnLabel(true);
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
