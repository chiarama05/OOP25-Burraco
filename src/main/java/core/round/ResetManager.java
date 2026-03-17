package core.round;

import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.player.PlayerImpl;

public interface ResetManager {

    /**
     * Reset the logic state of the game for a new round
     */
    void resetRound(PlayerImpl p1, PlayerImpl p2, DeckImpl deck, DiscardPile discardPile);

}
