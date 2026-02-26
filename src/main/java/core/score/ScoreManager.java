package core.score;

import model.player.Player;

/**
 * Interface for Burraco score calculation.
 */
public interface ScoreManager {

    /**
     * Calculates the final score of a player.
     */
    int calculateFinalScore(Player player);
}
