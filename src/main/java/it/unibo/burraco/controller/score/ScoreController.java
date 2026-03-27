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
public class ScoreController {

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
    private final Consumer<Runnable> uiThreadRunner;
    private final ViewProvider viewProvider;

    /**
     * Interface acting as a Factory for the ScoreView.
     * This abstraction allows for easier testing by enabling the injection 
     * of mock views instead of real GUI components.
     */
    public interface ViewProvider {
        ScoreView create(Player p1, Player p2, String n1, String n2, 
                         int target, Score s, TableView tv, boolean over);
    }

    public ScoreController(
            Score score,
            Player player1,
            Player player2,
            String nameP1,
            String nameP2,
            TableView tableView,
            GameController gameController,
            SoundController soundController,
            int targetScore,
            InitialDistributionView distributionView,
            Consumer<Runnable> uiThreadRunner,
            ViewProvider viewProvider) { 

        this.score           = score;
        this.player1         = player1;
        this.player2         = player2;
        this.nameP1          = nameP1;
        this.nameP2          = nameP2;
        this.tableView       = tableView;
        this.gameController  = gameController;
        this.soundController = soundController;
        this.targetScore     = targetScore;
        this.distributionView = distributionView;
        this.uiThreadRunner = uiThreadRunner;
        this.viewProvider = viewProvider;
    }

    /**
     * Triggers the end-of-round procedures: calculates points, updates match totals,
     * checks for victory, and triggers audio-visual feedback.
     */
    public void onRoundEnd() {
        // Calculate points earned in the current round
        int roundS1 = score.calculateFinalScore(player1);
        int roundS2 = score.calculateFinalScore(player2);

        // Update the total match score for both players
        player1.addPointsToMatch(roundS1);
        player2.addPointsToMatch(roundS2);

        // Check if any player has reached or exceeded the target match score
        boolean matchOver = player1.getMatchTotalScore() >= targetScore ||
                            player2.getMatchTotalScore() >= targetScore;

        if (matchOver) {
            // Use a separate thread to play victory sound without blocking logic
            Thread t = new Thread(() -> {
                soundController.playVictorySound(); 
                showScoreView(matchOver);
            });
            t.setDaemon(false);
            t.start();
        } else {
            soundController.playRoundEndSound(); 
            showScoreView(matchOver);
        }
    }

    /**
     * Initializes and displays the score view.
     * Sets up the callback for the "Next" action to reset the game for a new round.
     * @param matchOver true if the entire match has concluded.
     */
    private void showScoreView(boolean matchOver) {
        // Create the view via the provider (Factory)
        ScoreView view = viewProvider.create(
                player1, player2, nameP1, nameP2,
                targetScore, score, tableView, matchOver);

        // Define what happens when the user clicks 'Next'
        view.setOnNextAction(() -> {
            view.close();
            gameController.getTurnModel().resetForNewRound();

            // Instantiate a new RoundController to handle the next round's lifecycle
            RoundController roundController = new RoundControllerImpl(
                tableView,
                new ResetManagerImpl(),
                player1, player2,
                gameController,
                new InitialDistributionController(new DistributionManagerImpl()),
                distributionView
            );

            // Start the card distribution and turn cycle for the new round
            roundController.processNewRound();
            tableView.refreshTurnLabel(true);
        });
        view.display();
    }
}