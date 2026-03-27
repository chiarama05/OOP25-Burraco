package it.unibo.burraco.model.score;

import it.unibo.burraco.model.player.Player;

/**
 * Defines the contract for score calculation logic in a Burraco match.
 */
public interface Score {

    /** 
     * @return total final score including bonuses and penalties. 
     */
    int calculateFinalScore(Player player);

    /** 
     * @return total bonus points from burracos (clean/dirty). 
     */
    int calculateBurracoBonus(Player player);

    /** 
     * @return total point value of cards still in the player's hand. 
     */
    int calculateRemainingHandValue(Player player);

    /** 
     * @return the number of clean burracos found on the table. 
     */
    int countCleanBurraco(Player player); 

    /** 
     * @return the number of dirty burracos found on the table. 
     */
    int countDirtyBurraco(Player player);

    /** 
     * @return sum of card values currently placed on the table. 
     */
    int calculateOnlyCardsOnTable(Player player);

    int getCleanBurracoBonusValue();
    int getDirtyBurracoBonusValue();
    int getClosureBonusValue();
    int getNoPotPenalty();
}
