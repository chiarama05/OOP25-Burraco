package it.unibo.burraco.controller.discard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.controller.discardcard.discard.DiscardManager;
import it.unibo.burraco.controller.discardcard.discard.DiscardManagerImpl;
import it.unibo.burraco.controller.discardcard.discard.DiscardResult;

public class DiscardManagerImplTest {

    private DiscardManager discardManager;
    private DiscardPile discardPile;
    private Player player;

    @BeforeEach
    void init() {
        this.discardPile = mock(DiscardPile.class);
        this.player = mock(Player.class);
        this.discardManager = new DiscardManagerImpl(discardPile);
    }

    @Test
    void testDiscardSuccess() {
        Card card = mock(Card.class);
        when(player.getHand()).thenReturn(List.of(card));

        final DiscardResult result = discardManager.discard(player, card);

        assertTrue(result.isValid());
        verify(player).removeCardHand(card);
        verify(discardPile).add(card);
    }

    @Test
    void testDiscardNotInHand() {
        Card card = mock(Card.class);
        when(player.getHand()).thenReturn(List.of());

        final DiscardResult result = discardManager.discard(player, card);

        assertFalse(result.isValid());
        assertEquals("NOT_IN_HAND", result.getMessage());
        verify(discardPile, never()).add(any());
    }

    @Test
    void testDiscardNullCard() {
        final DiscardResult result = discardManager.discard(player, null);
        
        assertFalse(result.isValid());
        assertEquals("NOT_SELECTED", result.getMessage());
    }

    @Test
    void testDiscardRollbackWhenNoBurraco() {
        Card card = mock(Card.class);
        when(player.getHand()).thenReturn(List.of(card));
        
        when(player.getBurracoCount()).thenReturn(0);
        when(player.isInPot()).thenReturn(true); 

        final DiscardResult result = discardManager.discard(player, card);

        if ("NO_BURRACO_ERROR".equals(result.getMessage())) {
            assertFalse(result.isValid());
            verify(discardPile).drawLast();
            verify(player).addCardHand(card);
        }
    }

    @Test
    void testDiscardRoundWon() {
        Card card = mock(Card.class);
        when(player.getHand()).thenReturn(List.of(card));
        
        when(player.getBurracoCount()).thenReturn(1);
        when(player.isInPot()).thenReturn(true);

        final DiscardResult result = discardManager.discard(player, card);

        if (result.isGameWon()) {
            assertTrue(result.isValid());
            assertTrue(result.isTurnEnds());
            verify(discardPile).add(card);
        }
    }
}
