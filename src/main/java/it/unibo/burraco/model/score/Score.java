package it.unibo.burraco.model.score;

import it.unibo.burraco.model.player.Player;

/**
 * Interface for Burraco score calculation.
 */
public interface Score {

    /**
     * Calculates the final score of a player.
     */
    int calculateFinalScore(Player player);

    int calculateBurracoBonus(Player player);

    int calculateRemainingHandValue(Player player);

    int countCleanBurraco(Player player); 

    int countDirtyBurraco(Player player);
}
