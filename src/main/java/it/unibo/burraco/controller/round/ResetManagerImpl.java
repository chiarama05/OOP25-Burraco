package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

public class ResetManagerImpl implements ResetManager{

    @Override
    public void resetRound(Player p1, Player p2, Deck deck, DiscardPile discardPile) {
        p1.resetForNewRound();
        p2.resetForNewRound();
        deck.reset();
        discardPile.reset();
    }
}
