package model.discard;

import java.util.List;

import model.card.Card;

/**
 * Interface for a discard pile in a card game.
 * Defines the operations that can be performed on the discard pile.
 */

public interface DiscardPile {

    /**
     * Adds a single card to the discard pile.
     */
    void add(Card card);

    /**
     * Adds multiple cards to the discard pile.
     */
    void addAll(List<Card> cards);


     /**
     * Draws (removes and returns) the last card added to the discard pile.
     */
    Card drawLast();


    /**
     * Returns the list of cards currently in the discard pile.
     */
    List<Card> getCards();


     /**
     * Checks if the discard pile is empty.
     */
    boolean isEmpty();

     /**
     * Clears all cards from the discard pile.
     */
    void clear();
}
