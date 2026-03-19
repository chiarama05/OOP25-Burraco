package it.unibo.burraco.core.score;

import it.unibo.burraco.model.player.Player;

/**
 * Interface for Burraco score calculation.
 */
public interface ScoreManager {

    /**
     * Calculates the final score of a player.
     */
    int calculateFinalScore(Player player);

    int calculateBurracoBonus(Player player);

    int calculateRemainingHandValue(Player player);

    int countCleanBurraco(Player player); 

    int countDirtyBurraco(Player player);
}
