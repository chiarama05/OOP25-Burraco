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
import it.unibo.burraco.view.score.ScoreViewImpl;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.distribution.InitialDistributionView;

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
            Consumer<Runnable> uiThreadRunner) { 

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
    }

    public void onRoundEnd() {
    int roundS1 = score.calculateFinalScore(player1);
    int roundS2 = score.calculateFinalScore(player2);
    player1.addPointsToMatch(roundS1);
    player2.addPointsToMatch(roundS2);

    boolean matchOver = player1.getMatchTotalScore() >= targetScore ||
                        player2.getMatchTotalScore() >= targetScore;

    if (matchOver) {
        
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

private void showScoreView(boolean matchOver) {
    ScoreView view = new ScoreViewImpl(
        player1, player2, nameP1, nameP2,
        targetScore, score, tableView, matchOver);

    view.setOnNextAction(() -> {
        view.close();
        gameController.getTurnModel().resetForNewRound();

        RoundController roundController = new RoundControllerImpl(
            tableView,
            new ResetManagerImpl(),
            player1, player2,
            gameController,
            new InitialDistributionController(new DistributionManagerImpl()),
            distributionView
        );

        roundController.processNewRound();
        tableView.refreshTurnLabel(true);
    });

    view.display();
}
}