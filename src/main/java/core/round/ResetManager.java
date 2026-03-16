package core.round;

import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.player.PlayerImpl;

public interface ResetManager {

    /**
     * Resetta lo stato logico del gioco per un nuovo round.
     */
    void resetRound(PlayerImpl p1, PlayerImpl p2, DeckImpl deck, DiscardPile discardPile);

}
