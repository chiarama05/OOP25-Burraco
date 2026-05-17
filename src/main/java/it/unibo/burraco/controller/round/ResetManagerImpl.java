package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Concrete implementation of ResetManager.
 * It coordinates the reset sequence across the domain models.
 */
public final class ResetManagerImpl implements ResetManager {

    @Override
    public void resetRound(final Player p1,
                           final Player p2,
                           final Deck deck,
                           final DiscardPile discardPile) {
        p1.resetForNewRound();
        p2.resetForNewRound();
        deck.reset();
        discardPile.reset();
    }
}
