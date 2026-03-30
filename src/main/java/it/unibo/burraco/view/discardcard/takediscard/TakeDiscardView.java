package it.unibo.burraco.view.discardcard.takediscard;

import it.unibo.burraco.model.card.Card;
import java.util.List;

/**
 * Specialized interface for the graphical containers that need to be refreshed 
 * specifically when the discard pile is taken.
 */
public interface TakeDiscardView {

    /**
     * Refreshes the hand panel of the specified player.
     * 
     * @param isPlayer1 true for Player 1, false for Player 2.
     * @param hand the updated list of cards in the player's hand.
     */
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);

    /**
     * Updates the discard pile display.
     * 
     * @param cards the updated list of cards (usually empty after this action).
     */
    void updateDiscardPile(List<Card> cards);
}
