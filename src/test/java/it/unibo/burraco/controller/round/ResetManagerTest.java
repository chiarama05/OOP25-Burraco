package it.unibo.burraco.controller.round;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

class ResetManagerTest {

    @Test
    void testResetRoundOrchestration() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Deck deck = mock(Deck.class);
        DiscardPile discardPile = mock(DiscardPile.class);

        ResetManager resetManager = new ResetManagerImpl();

        resetManager.resetRound(p1, p2, deck, discardPile);

        verify(p1).resetForNewRound();
        verify(p2).resetForNewRound();
        verify(deck).reset();
        verify(discardPile).reset();
    }
}
