package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

public interface ResetManager {

    void resetRound(Player p1, Player p2, Deck deck, DiscardPile discardPile);
}
