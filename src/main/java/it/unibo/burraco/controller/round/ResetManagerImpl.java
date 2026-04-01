package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Concrete implementation of {@link ResetManager}.
 * It coordinates the reset sequence across the domain models.
 */
public final class ResetManagerImpl implements ResetManager {

    @Override
    public void resetRound(final Player p1,
                           final Player p2,
                           final Deck deck,
                           final DiscardPile discardPile) {
        // Clear player hands, side pots, and combinations
        p1.resetForNewRound();
        p2.resetForNewRound();

        // Re-shuffle and refill the deck
        deck.reset();

        // Clear the discard pile cards
        discardPile.reset();
    }
}
