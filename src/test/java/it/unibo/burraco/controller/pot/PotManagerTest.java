package it.unibo.burraco.controller.pot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn; 
import it.unibo.burraco.view.table.BurracoView;
import java.util.Collections;

class PotManagerTest {

    private static final String PLAYER_NAME = "Sofia";
    
    private Turn model;
    private BurracoView view;
    private Player player;
    private PotManager potManager;

    @BeforeEach
    void setUp() {
        this.model = mock(Turn.class);
        this.view = mock(BurracoView.class);
        this.player = mock(Player.class);
        this.potManager = new PotManager(this.model, this.view);
        when(this.model.getCurrentPlayer()).thenReturn(this.player);
        when(this.player.getName()).thenReturn(PLAYER_NAME);
    }

    @Test
    void testHandlePotWhenTakingPotDirectly() {
        when(this.player.isInPot()).thenReturn(true);
        when(this.model.isPlayer1Turn()).thenReturn(true);
        when(this.player.getHand()).thenReturn(Collections.emptyList());
        final boolean result = this.potManager.handlePot(false);
        assertTrue(result);
        verify(this.view).notifyPotTaken(PLAYER_NAME, false);
        verify(this.view).markPotTaken(true);
        verify(this.view).refreshHandPanel(eq(true), any());
    }

    @Test
    void testHandlePotWhenTakingPotWithDiscard() {
        when(this.player.isInPot()).thenReturn(true);
        when(this.model.isPlayer1Turn()).thenReturn(false);
        final boolean result = this.potManager.handlePot(true);
        assertTrue(result);
        verify(this.view).notifyPotTaken(PLAYER_NAME, true);
        verify(this.view).markPotTaken(false);
        verify(this.view, never()).refreshHandPanel(anyBoolean(), any());
    }

    @Test
    void testHandlePotWhenPlayerIsNotInPot() {
    when(this.player.isInPot()).thenReturn(false);
    final boolean result = this.potManager.handlePot(false); 
    assertFalse(result);
    verify(this.view, never()).notifyPotTaken(anyString(), anyBoolean());
    verify(this.view, never()).markPotTaken(anyBoolean());
    }
}
