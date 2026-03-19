package it.unibo.burraco.core.round;

import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.PlayerImpl;

public interface ResetManager {

    /**
     * Resetta lo stato logico del gioco per un nuovo round.
     */
    void resetRound(PlayerImpl p1, PlayerImpl p2, DeckImpl deck, DiscardPile discardPile);

}
