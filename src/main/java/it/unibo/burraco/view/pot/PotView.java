package it.unibo.burraco.view.pot;

import it.unibo.burraco.model.card.Card;
import java.util.List;


/**
 * View contract for pot-related UI updates.
 */
public interface PotView {

    /**
     * Displays a message informing the player that they have drawn their pot.
     *
     * @param playerName the name of the player who took the pot
     * @param isDiscard  true if the pot was triggered by a discard action
     */
    void showPotMessage(String playerName, boolean isDiscard);

    /**
     * Updates the visual indicator on the player's combination panel.
     *
     * @param isP1 true to update Player 1's panel, false for Player 2's
     */
    void markPotTaken(boolean isP1);

    /**
     * Refreshes the hand panel to display the updated list of cards.
     *
     * @param isPlayer1 true to refresh Player 1's hand panel, false for Player 2's
     * @param hand      the updated list of cards in the player's hand
     */
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
}
