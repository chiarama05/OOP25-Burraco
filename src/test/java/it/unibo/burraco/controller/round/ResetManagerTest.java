package it.unibo.burraco.controller.round;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

class ResetManagerTest {

    private ResetManager resetManager;
    private Player p1;
    private Player p2;
    private Deck deck;
    private DiscardPile discardPile;

    @BeforeEach
    void setUp() {
        resetManager = new ResetManagerImpl();
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        deck = mock(Deck.class);
        discardPile = mock(DiscardPile.class);
    }

    @Test
    void testResetRoundCallsAllModels() {
        resetManager.resetRound(p1, p2, deck, discardPile);
        verify(p1, times(1)).resetForNewRound();
        verify(p2, times(1)).resetForNewRound();
        verify(deck, times(1)).reset();
        verify(discardPile, times(1)).reset();
    }
}
