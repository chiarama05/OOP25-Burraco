package it.unibo.burraco.model.cards;

import java.util.List;

/**
 * Represents a generic deck of cards.
 * A Deck provides basic operations such as drawing, checking if empty
 * and retrieving the cards. Implementations define creation logic.
 */
public interface Deck {

    /**
     * Draws and removes a card from the top of the deck.
     * 
     * @return the drawn {@link Card}, or null if the deck is empty.
     */
    Card draw();

    /**
     * Checks whether the deck contains any cards.
     * 
     * @return true if the deck is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the list of cards currently in the deck.
     * 
     * @return a list of cards in the deck
     */
    List<Card> getCards();

    /**
     * Resets the deck to its initial state, regenerating and reshuffling all cards.
     */
    void reset();
}
