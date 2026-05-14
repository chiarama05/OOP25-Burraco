package it.unibo.burraco.controller.distribution;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Defines the contract for managing the initial card distribution logic.
 * Responsibility includes populating players' hands, creating side pots, 
 * and setting up the initial state of the discard pile and deck.
 */
public interface DistributionManager {

    /**
     * Executes the initial setup of a match.
     * This method modifies the state of the provided players, deck, and discard pile.
     *
     * @param player1          the first player to receive cards
     * @param player2          the second player to receive cards
     * @param deck             the source deck from which cards are drawn; 
     *                         will be depleted during distribution
     * @param modelDiscardPile the discard pile to be initialized with the first card
     * @throws IllegalStateException if the deck does not contain enough cards for setup
     */
    void distributeInitialCards(Player player1,
                                Player player2,
                                Deck deck,
                                DiscardPile modelDiscardPile);

}
