package core.resetround;

import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.player.PlayerImpl;

public class ResetManagerImpl implements ResetManager{

    @Override
    public void resetRound(PlayerImpl p1, PlayerImpl p2, DeckImpl deck, DiscardPile discardPile) {
        // 1. Reset logico dei giocatori (mani e combinazioni)
        p1.resetForNewRound();
        p2.resetForNewRound();

        // 2. Reset del mazzo
        deck.reset();

        // 3. Pulizia della pila degli scarti
        discardPile.getCards().clear();
    }

}
