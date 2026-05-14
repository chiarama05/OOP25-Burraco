package it.unibo.burraco.controller.pot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.table.pot.PotNotifier;
import it.unibo.burraco.view.table.pot.PotView;

class PotManagerTest {

    private PotManager potManager;
    private Turn model;
    private PotView view;
    private PotNotifier notifier;
    private Player player;

    @BeforeEach
    void setUp() {
        model = mock(Turn.class);
        view = mock(PotView.class);
        notifier = mock(PotNotifier.class);
        player = mock(Player.class);

        when(model.getCurrentPlayer()).thenReturn(player);
        
        potManager = new PotManager(model, view, notifier);
    }

    @Test
    void testHandlePotWithDiscard() {
        when(player.isInPot()).thenReturn(true);
        when(player.getName()).thenReturn("Alice");
        when(model.isPlayer1Turn()).thenReturn(true);

        boolean result = potManager.handlePot(true);

        assertTrue(result);
        
        verify(notifier).notifyPotTaken("Alice", true);
        verify(view).markPotTaken(true);
        
        verify(view, never()).refreshHandPanel(anyBoolean(), any());
    }

    @Test
    void testHandlePotOnTheFly() {
        when(player.isInPot()).thenReturn(true);
        when(model.isPlayer1Turn()).thenReturn(false);

        boolean result = potManager.handlePot(false);

        assertTrue(result);
        verify(view).markPotTaken(false);
        
        verify(view).refreshHandPanel(eq(false), any());
    }

    @Test
    void testHandlePotWhenNotEligible() {
        when(player.isInPot()).thenReturn(false);

        boolean result = potManager.handlePot(true);

        assertFalse(result);
        
        verifyNoInteractions(notifier);
        verifyNoInteractions(view);
    }
}
