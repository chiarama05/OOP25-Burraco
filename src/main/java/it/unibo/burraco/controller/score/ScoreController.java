package it.unibo.burraco.controller.score;

import it.unibo.burraco.controller.round.RoundEndHandler;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;

/**
 * Controller responsible for the high-level management of scoring at the end of a round.
 * It acts as an intermediary that triggers the mathematical calculation of scores 
 * within the Model and then delegates the UI presentation to the {@link RoundEndHandler}.
 */
public final class ScoreController {

    private final Score score;
    private final Player player1;
    private final Player player2;
    private final RoundEndHandler roundEndHandler;

    /**
     * Constructs a ScoreController.
     * 
     * @param score            the model component containing scoring rules and logic
     * @param player1          the first player model
     * @param player2          the second player model
     * @param roundEndHandler  the handler responsible for the visual end-of-round sequence
     */
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

    /**
     * Forwards the new round action to the encapsulated {@link RoundEndHandler}.
     * 
     * @param action the {@link Runnable} to be executed for a new round
     */
    public void setOnNewRound(final Runnable action) {
        this.roundEndHandler.setOnNewRound(action);
    }

    /**
     * Handles the transition of scores from the current round to the match total.
     * It calculates the round points for each player, updates their total match 
     * standing, and triggers the final display sequence.
     */
    public void onRoundEnd() {
        final int roundS1 = score.calculateFinalScore(player1);
        final int roundS2 = score.calculateFinalScore(player2);
        player1.addPointsToMatch(roundS1);
        player2.addPointsToMatch(roundS2);
        roundEndHandler.handle();
    }
}
