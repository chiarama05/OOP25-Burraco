package it.unibo.burraco.controller.draw;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Card mockCard = mock(Card.class);
        when(deck.draw()).thenReturn(mockCard);

        DrawResult result = drawManager.drawFromDeck(player, deck);

        assertEquals(DrawResult.Status.SUCCESS, result.getStatus());
        assertEquals(mockCard, result.getDrawnCard());
        assertTrue(drawManager.hasDrawn());
        verify(player).addCardHand(mockCard);
    }

    @Test
    void testDrawFromDeckEmpty() {
        when(deck.draw()).thenReturn(null);

        DrawResult result = drawManager.drawFromDeck(player, deck);

        assertEquals(DrawResult.Status.EMPTY_DECK, result.getStatus());
        assertFalse(drawManager.hasDrawn());
    }

    @Test
    void testAlreadyDrawnFromDeck() {
        when(deck.draw()).thenReturn(mock(Card.class));
        
        drawManager.drawFromDeck(player, deck);
        
        DrawResult result = drawManager.drawFromDeck(player, deck);

        assertEquals(DrawResult.Status.ALREADY_DRAWN, result.getStatus());
        verify(deck, times(1)).draw(); 
    }

    @Test
    void testDrawFromDiscardSuccess() {
        List<Card> discardPile = new ArrayList<>();
        Card c1 = mock(Card.class);
        Card c2 = mock(Card.class);
        discardPile.add(c1);
        discardPile.add(c2);

        List<Card> hand = new ArrayList<>();
        when(player.getHand()).thenReturn(hand);

        DrawResult result = drawManager.drawFromDiscard(player, discardPile);

        assertEquals(DrawResult.Status.SUCCESS_MULTIPLE, result.getStatus());
        assertTrue(hand.contains(c1));
        assertTrue(hand.contains(c2));
        assertTrue(discardPile.isEmpty());
        assertTrue(drawManager.hasDrawn());
    }

    @Test
    void testDrawFromEmptyDiscard() {
        List<Card> emptyDiscard = new ArrayList<>();

        DrawResult result = drawManager.drawFromDiscard(player, emptyDiscard);

        assertEquals(DrawResult.Status.EMPTY_DISCARD, result.getStatus());
        assertFalse(drawManager.hasDrawn());
    }

    @Test
    void testResetTurn() {
        when(deck.draw()).thenReturn(mock(Card.class));
        
        drawManager.drawFromDeck(player, deck);
        assertTrue(drawManager.hasDrawn());

        drawManager.resetTurn();
        assertFalse(drawManager.hasDrawn());
        
        DrawResult result = drawManager.drawFromDeck(player, deck);
        assertEquals(DrawResult.Status.SUCCESS, result.getStatus());
    }
}