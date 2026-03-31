package it.unibo.burraco.view.hand;

import it.unibo.burraco.model.card.Card;
import java.util.List;
import java.util.Set;

/**
 * Interface representing the visual component for a player's hand.
 * It defines methods for displaying cards, managing selections, and
 * notifying listeners about user interactions.
 */
public interface HandView {

    /**
     * Refreshes the visual representation of the hand based on the provided list of cards.
     *
     * @param hand the current list of {@link Card} objects to display
     */
    void refreshHand(List<Card> hand);

    /**
     * Returns the set of cards currently selected by the user in the view.
     *
     * @return a {@link Set} of selected {@link Card} objects
     */
    Set<Card> getSelectedCards();

    /**
     * Resets the selection status, deselecting all cards currently in the hand.
     */
    void clearSelection();

    /**
     * Sets a listener to handle card selection events.
     *
     * @param listener the {@link CardSelectionListener} to notify on interaction
     */
    void setCardSelectionListener(CardSelectionListener listener);

    /**
     * Inner interface for handling card selection events within the HandView.
     */
    @FunctionalInterface
    interface CardSelectionListener {

        /**
         * Triggered when a specific card is selected or interacted with.
         *
         * @param c the {@link Card} that was selected
         */
        void onCardSelected(Card c);
    }
}
