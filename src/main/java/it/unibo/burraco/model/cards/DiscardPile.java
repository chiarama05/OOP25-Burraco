package it.unibo.burraco.model.cards;

import java.util.List;

/**
 * Interface representing the discard pile in the Burraco game.
 * It defines the operations for managing discarded cards.
 */
public interface DiscardPile {

    /**
     * Adds a single card to the top of the discard pile.
     *
     * @param card the card to be discarded.
     */
    void add(Card card);

    /**
     * Adds a collection of cards to the discard pile.
     *
     * @param cards the list of cards to add.
     */
    void addAll(List<Card> cards);

    /**
     * Removes and returns the last card added to the pile (the top card).
     *
     * @return the removed {@link Card}, or null if the pile is empty.
     */
    Card drawLast();

    /**
     * Provides the list of cards currently in the discard pile.
     *
     * @return a list containing the cards in discard order.
     */
    List<Card> getCards();

    /**
     * Checks whether the discard pile contains any cards.
     *
     * @return true if the pile is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Resets the discard pile by removing all cards,
     * restoring it to its initial empty state.
     */
    void reset();
}
