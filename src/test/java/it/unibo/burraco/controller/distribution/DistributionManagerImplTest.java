package it.unibo.burraco.controller.distribution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

class DistributionManagerImplTest {

    private static final int HAND_SIZE = 11;
    private static final int POT_SIZE = 11;
    private static final int TOTAL_DRAWS = 45;

    private DistributionManagerImpl distManager;
    private Player p1;
    private Player p2;
    private Deck deck;
    private DiscardPile discardPile;

    @BeforeEach
    void setUp() {
        this.distManager = new DistributionManagerImpl();
        this.p1 = mock(Player.class);
        this.p2 = mock(Player.class);
        this.deck = mock(Deck.class);
        this.discardPile = mock(DiscardPile.class);
        when(this.deck.draw()).thenReturn(mock(Card.class));
    }

    @Test
    void testDistributeInitialCards() {
        this.distManager.distributeInitialCards(this.p1, this.p2, this.deck, this.discardPile);
        verify(this.p1, times(HAND_SIZE)).addCardHand(any(Card.class));
        verify(this.p2, times(HAND_SIZE)).addCardHand(any(Card.class));
        verify(this.p1).addToPot(argThat(list -> list.size() == POT_SIZE));
        verify(this.p2).addToPot(argThat(list -> list.size() == POT_SIZE));
        verify(this.discardPile).add(any(Card.class));
        verify(this.deck, times(TOTAL_DRAWS)).draw();
    }
}
