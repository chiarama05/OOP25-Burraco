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
        this.resetManager = new ResetManagerImpl();
        this.p1 = mock(Player.class);
        this.p2 = mock(Player.class);
        this.deck = mock(Deck.class);
        this.discardPile = mock(DiscardPile.class);
    }

    @Test
    void testResetRoundCallsAllModels() {
        this.resetManager.resetRound(this.p1, this.p2, this.deck, this.discardPile);
    
        verify(this.p1, times(1)).resetForNewRound();
        verify(this.p2, times(1)).resetForNewRound();
        verify(this.deck, times(1)).reset();
        verify(this.discardPile, times(1)).reset();
    }
}
