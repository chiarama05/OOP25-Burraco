package it.unibo.burraco.model.score;

import it.unibo.burraco.model.player.Player;

/**
 * Defines the contract for score calculation logic in a Burraco match.
 */
public interface Score {

    /**
     * Calculates the total final score including bonuses and penalties.
     * @param player the player to evaluate.
     * @return total final score.
     */
    int calculateFinalScore(Player player);

    /**
     * Calculates the total bonus points from burracos (clean/dirty).
     * @param player the player to evaluate.
     * @return total bonus points from burracos.
     */
    int calculateBurracoBonus(Player player);

    /**
     * Calculates the total point value of cards still in the player's hand.
     * @param player the player to evaluate.
     * @return total value of remaining hand cards.
     */
    int calculateRemainingHandValue(Player player);

    /**
     * Counts the number of clean burracos found on the table.
     * @param player the player to evaluate.
     * @return the number of clean burracos.
     */
    int countCleanBurraco(Player player);

    /**
     * Counts the number of dirty burracos found on the table.
     * @param player the player to evaluate.
     * @return the number of dirty burracos.
     */
    int countDirtyBurraco(Player player);

    /**
     * Calculates the sum of card values currently placed on the table.
     * @param player the player to evaluate.
     * @return sum of card values on the table.
     */
    int calculateOnlyCardsOnTable(Player player);

    /**
     * Returns the bonus value awarded for a clean burraco.
     * @return clean burraco bonus value.
     */
    int getCleanBurracoBonusValue();

    /**
     * Returns the bonus value awarded for a dirty burraco.
     * @return dirty burraco bonus value.
     */
    int getDirtyBurracoBonusValue();

    /**
     * Returns the bonus value awarded for closing the round.
     * @return closure bonus value.
     */
    int getClosureBonusValue();

    /**
     * Returns the penalty applied when a player never collected their pot.
     * @return no-pot penalty value (negative).
     */
    int getNoPotPenalty();
}