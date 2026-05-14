package it.unibo.burraco.controller.round;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

class ResetManagerTest {

    @Test
    void testResetRoundOrchestration() {
        final Player p1 = mock(Player.class);
        final Player p2 = mock(Player.class);
        final Deck deck = mock(Deck.class);
        final DiscardPile discardPile = mock(DiscardPile.class);

        final ResetManager resetManager = new ResetManagerImpl();

        resetManager.resetRound(p1, p2, deck, discardPile);

        verify(p1).resetForNewRound();
        verify(p2).resetForNewRound();
        verify(deck).reset();
        verify(discardPile).reset();
    }
}
