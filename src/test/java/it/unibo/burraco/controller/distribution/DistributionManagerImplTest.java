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
        distManager = new DistributionManagerImpl();
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        deck = mock(Deck.class);
        discardPile = mock(DiscardPile.class);
        when(deck.draw()).thenReturn(mock(Card.class));
    }

    @Test
    void testDistributeInitialCards() {
        distManager.distributeInitialCards(p1, p2, deck, discardPile);
        verify(p1, times(11)).addCardHand(any(Card.class));
        verify(p2, times(11)).addCardHand(any(Card.class));
        verify(p1).addToPot(argThat(list -> list.size() == 11));
        verify(p2).addToPot(argThat(list -> list.size() == 11));
        verify(discardPile).add(any(Card.class));
        verify(deck, times(45)).draw();
    }
}
