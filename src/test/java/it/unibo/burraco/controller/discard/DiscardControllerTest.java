package it.unibo.burraco.controller.discard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.discardcard.discard.DiscardController;
import it.unibo.burraco.controller.discardcard.discard.DiscardManagerImpl;
import it.unibo.burraco.controller.discardcard.discard.DiscardResult;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.discard.DiscardPile;

import java.util.List;
import java.util.Set;

class DiscardControllerTest {

    private DiscardController controller;
    private DrawManager drawManager;
    private DiscardPile discardPile;
    private Player currentPlayer;

    @BeforeEach
    void init() {
        this.discardPile = mock(DiscardPile.class);
        this.drawManager = mock(DrawManager.class);
        final Turn turnModel = mock(Turn.class);
        this.currentPlayer = mock(Player.class);

        when(turnModel.getCurrentPlayer()).thenReturn(this.currentPlayer);

        final DiscardManagerImpl discardManager = new DiscardManagerImpl(this.discardPile);
        this.controller = new DiscardController(
            discardManager,
            mock(TurnController.class),
            mock(PotManager.class),
            mock(ClosureManager.class),
            this.drawManager,
            turnModel
        );
    }

    @Test
    void testTryDiscardFailsWhenNotDrawn() {
        when(this.drawManager.hasDrawn()).thenReturn(false);

        final DiscardResult result = this.controller.tryDiscard(Set.of(mock(Card.class)));

        assertFalse(result.isValid(), "Discard should be invalid if player hasn't drawn");
        assertEquals("must_draw", result.getMessage());
    }

    @Test
    void testTryDiscardFailsWithMultipleCards() {
        when(this.drawManager.hasDrawn()).thenReturn(true);

        final Set<Card> multipleCards = Set.of(mock(Card.class), mock(Card.class));
        final DiscardResult result = this.controller.tryDiscard(multipleCards);

        assertFalse(result.isValid(), "Discard should be invalid if multiple cards are selected");
        assertEquals("select_one", result.getMessage());
    }

    @Test
    void testTryDiscardSuccessFlow() {
        when(this.drawManager.hasDrawn()).thenReturn(true);
        final Card cardToDiscard = mock(Card.class);
        when(this.currentPlayer.getHand()).thenReturn(List.of(cardToDiscard));

        final DiscardResult result = this.controller.tryDiscard(Set.of(cardToDiscard));

        assertTrue(result.isValid(), "Discard should be valid");
        verify(this.discardPile).add(cardToDiscard);
    }
}
