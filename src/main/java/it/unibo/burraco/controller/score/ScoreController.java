package it.unibo.burraco.controller.score;

import it.unibo.burraco.controller.SoundController;
import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.round.ResetManagerImpl;
import it.unibo.burraco.controller.round.RoundController;
import it.unibo.burraco.controller.round.RoundControllerImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.score.ScoreViewImpl;
import it.unibo.burraco.view.table.TableView;

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

    public ScoreController(
            Score score,
            Player player1,
            Player player2,
            String nameP1,
            String nameP2,
            TableView tableView,
            GameController gameController,
            SoundController soundController,
            int targetScore) {

        this.score          = score;
        this.player1        = player1;
        this.player2        = player2;
        this.nameP1         = nameP1;
        this.nameP2         = nameP2;
        this.tableView      = tableView;
        this.gameController = gameController;
        this.soundController = soundController;
        this.targetScore    = targetScore;
    }

    public void onRoundEnd() {

        int roundS1 = score.calculateFinalScore(player1);
        int roundS2 = score.calculateFinalScore(player2);

        player1.addPointsToMatch(roundS1);
        player2.addPointsToMatch(roundS2);

        RoundController roundController = new RoundControllerImpl(
            tableView,
            new ResetManagerImpl(),
            player1,
            player2,
            gameController,
            new InitialDistributionController(new DistributionManagerImpl())
        );

        new ScoreViewImpl(
            player1, player2,
            nameP1, nameP2,
            targetScore,
            score,
            tableView,
            gameController,
            roundController,
            soundController
        ).display();
    }
}
