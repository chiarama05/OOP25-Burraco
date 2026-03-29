package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Concrete implementation of ResetManager.
 * It coordinates the reset sequence across the domain models.
 */
public class ResetManagerImpl implements ResetManager{

    @Override
    public void resetRound(Player p1, Player p2, Deck deck, DiscardPile discardPile) {
        // Clear player hands, side pots, and combinations
        p1.resetForNewRound();
        p2.resetForNewRound();

        // Re-shuffle and refill the deck
        deck.reset();

        // Clear the discard pile cards
        discardPile.reset();
    }
}
