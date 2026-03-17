package core.round;

import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.player.PlayerImpl;

public class ResetManagerImpl implements ResetManager{

    @Override
    public void resetRound(PlayerImpl p1, PlayerImpl p2, DeckImpl deck, DiscardPile discardPile) {
        // 1. Logic reset of the player (hands and combinations)
        p1.resetForNewRound();
        p2.resetForNewRound();

        // 2. Deck's reset
        deck.reset();

        // 3. Discard pile's reset
        discardPile.getCards().clear();
    }

}
