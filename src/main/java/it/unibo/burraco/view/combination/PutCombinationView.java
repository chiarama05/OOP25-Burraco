package it.unibo.burraco.view.combination;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

/**
 * Interface defining the callback methods for the combination placement view.
 * It provides a contract for updating the UI based on the outcome of a player
 * attempting to lay down a new combination.
 */
public interface PutCombinationView {
    /**
     * Called when a combination is successfully placed on the table.
     *
     * @param combo the list of cards forming the successful combination
     * @param isP1 true if the action was performed by Player 1
     * @param current the player object containing the updated hand and state
     */
    void onCombinationSuccess(List<Card> combo, boolean isP1, Player current);

    /**
     * Called when a combination is placed and triggers the taking of a pot.
     *
     * @param combo the cards placed on the table
     * @param isP1 true if performed by Player 1
     * @param current the player object, used to refresh the new hand from the pot
     */
    void onCombinationTakePot(List<Card> combo, boolean isP1, Player current);

    /**
     * Called when a combination is placed and leads to the closure of the round.
     *
     * @param combo   the final cards placed
     * @param isP1    true if performed by Player 1
     * @param current the player object
     */
    void onCombinationClose(List<Card> combo, boolean isP1, Player current);
}
