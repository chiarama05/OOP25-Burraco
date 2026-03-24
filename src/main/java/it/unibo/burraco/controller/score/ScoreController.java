package it.unibo.burraco.controller.score;

import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.score.ScoreViewImpl;
import it.unibo.burraco.view.table.TableViewImpl;

public class ScoreController {

    private final Score score;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final String nameP1;
    private final String nameP2;
    private final TableViewImpl tableView;
    private final GameController gameController;
    private final int targetScore;

    public ScoreController(
            Score score,
            PlayerImpl player1,
            PlayerImpl player2,
            String nameP1,
            String nameP2,
            TableViewImpl tableView,
            GameController gameController,
            int targetScore) {

        this.score          = score;
        this.player1        = player1;
        this.player2        = player2;
        this.nameP1         = nameP1;
        this.nameP2         = nameP2;
        this.tableView      = tableView;
        this.gameController = gameController;
        this.targetScore    = targetScore;
    }

    public void onRoundEnd() {

        int roundS1 = score.calculateFinalScore(player1);
        int roundS2 = score.calculateFinalScore(player2);

        player1.addPointsToMatch(roundS1);
        player2.addPointsToMatch(roundS2);

        new ScoreViewImpl(
            player1, player2,
            nameP1, nameP2,
            targetScore,
            tableView,
            gameController
        ).display();
    }
}
