package it.unibo.burraco.view.table.hand;

import java.util.List;

import it.unibo.burraco.model.cards.Card;

/**
 * Interface representing the visual component for a player's hand.
 * It defines methods for displaying cards, managing selections, and
 * notifying listeners about user interactions.
 */
public interface HandView {

    /**
     * Refreshes the visual representation of the hand with the provided cards.
     *
     * @param hand the current list of {@link Card} to display
     */
    void refreshHand(List<Card> hand);

    /**
     * Returns the list of cards currently selected by the user in the view.
     *
     * @return the selected cards
     */
    List<Card> getSelectedCards();

    /**
     * Deselects all cards currently selected in the hand.
     */
    void clearSelection();

    /**
     * Sets a listener to handle card selection events.
     *
     * @param listener the listener to notify on card interaction
     */
    void setCardSelectionListener(CardSelectionListener listener);

    /**
     * Updates the hand with a new list of cards.
     *
     * @param hand the new list of cards to display
     */
    void updateHand(List<Card> hand);

    /**
     * Functional interface for handling card selection events within the HandView.
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
