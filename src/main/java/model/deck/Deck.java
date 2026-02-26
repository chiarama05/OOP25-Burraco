package model.deck;

import java.util.*;

import model.card.Card;

/**
 * Represents a generic deck of cards.
 * 
 * A Deck provides basic operations such as:
 * - Drawing a card
 * - Checking if the deck is empty
 * - Retrieving the current list of cards
 * 
 * Implementations of this interface define how
 * the deck is created and managed.
 */
public interface Deck {
    /**
     * Draws and removes a card from the deck.
     *
     * @return the drawn card.
     * The behavior when the deck is empty depends on the implementation.
     */
    Card draw();


    /**
     * Checks whether the deck contains any cards.
     *
     * @return true if the deck is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the list of cards currently in the deck.
     *
     * Note: Depending on the implementation,
     * this may return the internal list directly
     * or a protected copy of it.
     *
     * @return a list of cards in the deck.
     */
    List<Card> getCards();
}
