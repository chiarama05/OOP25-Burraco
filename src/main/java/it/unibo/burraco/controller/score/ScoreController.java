package it.unibo.burraco.controller.score;

import it.unibo.burraco.controller.round.RoundEndHandler;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;

/**
 * Responsible only for score calculation and delegating
 * end-of-round logic to RoundEndHandler.
 */
public final class ScoreController {

    private final Score score;
    private final Player player1;
    private final Player player2;
    private final RoundEndHandler roundEndHandler;

    public ScoreController(
            final Score score,
            final Player player1,
            final Player player2,
            final RoundEndHandler roundEndHandler) {
        this.score = score;
        this.player1 = player1;
        this.player2 = player2;
        this.roundEndHandler = roundEndHandler;
    }

    public void setOnNewRound(final Runnable action) {
        this.roundEndHandler.setOnNewRound(action);
    }
    /**
     * Calculates round scores, updates match totals, then delegates
     * end-of-round presentation to RoundEndHandler.
     */
    public void onRoundEnd() {
        final int roundS1 = score.calculateFinalScore(player1);
        final int roundS2 = score.calculateFinalScore(player2);
        player1.addPointsToMatch(roundS1);
        player2.addPointsToMatch(roundS2);
        roundEndHandler.handle();
    }
}
