package it.unibo.burraco.controller.draw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;

class DrawManagerTest {

    private DrawManager drawManager;
    private Player player;
    private Deck deck;

    @BeforeEach
    void init() {
        drawManager = new DrawManager();
        player = mock(Player.class);
        deck = mock(Deck.class);
    }

    @Test
    void testDrawFromDeckSuccess() {
        final Card mockCard = mock(Card.class);
        when(deck.draw()).thenReturn(mockCard);
        final DrawResult result = drawManager.drawFromDeck(player, deck);
        assertEquals(DrawResult.Status.SUCCESS, result.getStatus());
        assertEquals(mockCard, result.getDrawnCard());
        assertTrue(drawManager.hasDrawn());
        verify(player).addCardHand(mockCard);
    }

    @Test
    void testDrawFromDeckEmpty() {
        when(deck.draw()).thenReturn(null);
        final DrawResult result = drawManager.drawFromDeck(player, deck);
        assertEquals(DrawResult.Status.EMPTY_DECK, result.getStatus());
        assertFalse(drawManager.hasDrawn());
    }

    @Test
    void testAlreadyDrawnFromDeck() {
        when(deck.draw()).thenReturn(mock(Card.class));
        drawManager.drawFromDeck(player, deck);
        final DrawResult result = drawManager.drawFromDeck(player, deck);
        assertEquals(DrawResult.Status.ALREADY_DRAWN, result.getStatus());
        verify(deck, times(1)).draw(); 
    }

    @Test
    void testDrawFromDiscardSuccess() {
        final List<Card> discardPile = new ArrayList<>();
        final Card c1 = mock(Card.class);
        final Card c2 = mock(Card.class);
        discardPile.add(c1);
        discardPile.add(c2);
        
        final List<Card> hand = new ArrayList<>();
        when(this.player.getHand()).thenReturn(hand);
        
        final DrawResult result = this.drawManager.drawFromDiscard(this.player, discardPile);
        
        assertEquals(DrawResult.Status.SUCCESS_MULTIPLE, result.getStatus());
        assertTrue(hand.contains(c1));
        assertTrue(hand.contains(c2));
        assertTrue(discardPile.isEmpty());
        assertTrue(this.drawManager.hasDrawn());
    }

    @Test
    void testDrawFromEmptyDiscard() {
        final List<Card> emptyDiscard = new ArrayList<>();
        
        final DrawResult result = this.drawManager.drawFromDiscard(this.player, emptyDiscard);
        
        assertEquals(DrawResult.Status.EMPTY_DISCARD, result.getStatus());
        assertFalse(this.drawManager.hasDrawn());
    }

    @Test
    void testResetTurn() {
        when(this.deck.draw()).thenReturn(mock(Card.class));
        
        this.drawManager.drawFromDeck(this.player, this.deck);
        assertTrue(this.drawManager.hasDrawn());
        
        this.drawManager.resetTurn();
        assertFalse(this.drawManager.hasDrawn());
        
        final DrawResult result = this.drawManager.drawFromDeck(this.player, this.deck);
        assertEquals(DrawResult.Status.SUCCESS, result.getStatus());
    }
}
