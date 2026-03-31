package it.unibo.burraco.controller.distribution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

class DistributionManagerImplTest {

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
        verify(this.p1, times(11)).addCardHand(any(Card.class));
        verify(this.p2, times(11)).addCardHand(any(Card.class));
        verify(this.p1).addToPot(argThat(list -> list.size() == 11));
        verify(this.p2).addToPot(argThat(list -> list.size() == 11));
        verify(this.discardPile).add(any(Card.class));
        verify(this.deck, times(45)).draw();
    }
}