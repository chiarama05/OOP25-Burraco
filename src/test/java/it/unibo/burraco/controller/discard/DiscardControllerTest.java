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
    private Turn turnModel;
    private DiscardManagerImpl discardManager;
    private DiscardPile discardPile;
    private Player currentPlayer;

    @BeforeEach
    void init() {
        discardPile = mock(DiscardPile.class);
        discardManager = new DiscardManagerImpl(discardPile);
        drawManager = mock(DrawManager.class);
        turnModel = mock(Turn.class);
        currentPlayer = mock(Player.class);
        when(turnModel.getCurrentPlayer()).thenReturn(currentPlayer);
        this.controller = new DiscardController(
            discardManager, 
            mock(TurnController.class),
            mock(PotManager.class),
            mock(ClosureManager.class),
            drawManager,
            turnModel
        );
    }

    @Test
    void testMustDrawBeforeDiscard() {
        when(drawManager.hasDrawn()).thenReturn(false);
        final DiscardResult result = controller.tryDiscard(Set.of(mock(Card.class)));
        assertFalse(result.isValid());
        assertEquals("must_draw", result.getMessage());
    }

    @Test
    void testMustSelectExactlyOneCard() {
        when(drawManager.hasDrawn()).thenReturn(true);
        final DiscardResult result = controller.tryDiscard(Set.of(mock(Card.class), mock(Card.class)));
        assertFalse(result.isValid());
        assertEquals("select_one", result.getMessage());
    }

    @Test
    void testSuccessfulDiscardFlow() {
        when(drawManager.hasDrawn()).thenReturn(true);
        final Card card = mock(Card.class);
        when(currentPlayer.getHand()).thenReturn(List.of(card));
        final DiscardResult result = controller.tryDiscard(Set.of(card));
        assertTrue(result.isValid());
        verify(discardPile).add(card); 
    }
}