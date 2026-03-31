package it.unibo.burraco.controller.pot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.pot.PotView;

class PotManagerTest {

    private Turn turnModel;
    private PotView potView;
    private PotManager potManager;
    private Player player;

    @BeforeEach
    void setUp() {
        this.turnModel = mock(Turn.class);
        this.potView = mock(PotView.class);
        this.potManager = new PotManager(this.turnModel, this.potView);

        this.player = new PlayerImpl("TestPlayer");

        when(this.turnModel.isPlayer1Turn()).thenReturn(true);
        when(this.turnModel.getCurrentPlayer()).thenReturn(this.player);
    }

    @Test
    void testHandlePotTakesAndRefreshesWhenHandEmptyPotNotTakenNotDiscard() {
        final boolean result = this.potManager.handlePot(false);

        assertTrue(result, "Must return true when pot is taken");
        assertTrue(this.player.isInPot(), "Player must be marked as having taken the pot");

        verify(this.potView).showPotMessage(eq(this.player.getName()), eq(false));
        verify(this.potView).markPotTaken(eq(true));
        verify(this.potView).refreshHandPanel(eq(true), anyList());
    }

    @Test
    void testHandlePotTakesWithoutRefreshWhenHandEmptyPotNotTakenIsDiscard() {
        final boolean result = this.potManager.handlePot(true);

        assertTrue(result, "Must return true when pot is taken");
        assertTrue(this.player.isInPot());

        verify(this.potView).showPotMessage(eq(this.player.getName()), eq(true));
        verify(this.potView).markPotTaken(anyBoolean());
        verify(this.potView, never()).refreshHandPanel(anyBoolean(), anyList());
    }

    @Test
    void testHandlePotDoesNothingWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl("♠", "5"));

        final boolean result = this.potManager.handlePot(false);

        assertFalse(result, "Must return false when hand is not empty");
        assertFalse(this.player.isInPot(), "Player must not be marked as in-pot");

        verify(this.potView, never()).showPotMessage(anyString(), anyBoolean());
        verify(this.potView, never()).markPotTaken(anyBoolean());
        verify(this.potView, never()).refreshHandPanel(anyBoolean(), anyList());
    }

    @Test
    void testHandlePotDoesNothingWhenPotAlreadyTaken() {
        this.player.setInPot(true);

        final boolean result = this.potManager.handlePot(false);

        assertFalse(result, "Must return false when pot already taken");

        verify(this.potView, never()).showPotMessage(anyString(), anyBoolean());
        verify(this.potView, never()).markPotTaken(anyBoolean());
        verify(this.potView, never()).refreshHandPanel(anyBoolean(), anyList());
    }

    @Test
    void testHandlePotForwardsPlayer2TurnFlagToView() {
        when(this.turnModel.isPlayer1Turn()).thenReturn(false);

        this.potManager.handlePot(false);

        verify(this.potView).markPotTaken(false);
        verify(this.potView).refreshHandPanel(eq(false), anyList());
    }

    @Test
    void testHandlePotForwardsIsDiscardFlagToShowPotMessage() {
        this.potManager.handlePot(true);
        verify(this.potView).showPotMessage(this.player.getName(), true);

        this.player.setInPot(false);
        this.potManager.handlePot(false);
        verify(this.potView).showPotMessage(this.player.getName(), false);
    }
}
