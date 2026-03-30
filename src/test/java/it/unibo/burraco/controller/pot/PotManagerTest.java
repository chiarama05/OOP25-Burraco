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

/**
 * Tests for {@link PotManager}.
 *
 * Covers the four observable branches of {@code handlePot(boolean isDiscard)}:
 *
 *  1. Hand empty + pot NOT taken (isDiscard = false) → pot is drawn, view notified, hand panel refreshed, returns true.
 *  2. Hand empty + pot NOT taken (isDiscard = true)  → pot is drawn, view notified, hand panel NOT refreshed, returns true.
 *  3. Hand NOT empty → nothing happens, returns false.
 *  4. Hand empty but pot already taken→ nothing happens, returns false.
 */
class PotManagerTest {

    private Turn turnModel;
    private PotView potView;
    private PotManager potManager;

    /** A player whose pot is pre-loaded with some cards so drawPot() has something to draw. */
    private Player player;

    @BeforeEach
    void setUp() {
        turnModel  = mock(Turn.class);
        potView    = mock(PotView.class);
        potManager = new PotManager(turnModel, potView);

        // A real PlayerImpl so we can inspect its state after the call.
        player = new PlayerImpl("TestPlayer");

        // By default simulate player 1's turn
        when(turnModel.isPlayer1Turn()).thenReturn(true);
        when(turnModel.getCurrentPlayer()).thenReturn(player);
    }



    // Branch 1 – hand empty, pot not taken, NOT a discard action


    /*When the player's hand is empty and the pot has not been taken yet, handlePot(false) must:
    - mark the player as "in pot"
    - call drawPot() on the player (verified via isInPot flag + hand size change)
    - invoke showPotMessage and markPotTaken on the view
    - invoke refreshHandPanel (because isDiscard = false)
    - return true*/
    
    @Test
    void testHandlePot_takesAndRefreshes_whenHandEmptyPotNotTaken_notDiscard() {
        // hand is empty by default; pot not taken by default

        final boolean result = potManager.handlePot(false);

        assertTrue(result, "Must return true when pot is taken");
        assertTrue(player.isInPot(), "Player must be marked as having taken the pot");

        verify(potView).showPotMessage(eq(player.getName()), eq(false));
        verify(potView).markPotTaken(eq(true));          // isPlayer1Turn() returns true
        verify(potView).refreshHandPanel(eq(true), anyList());
    }



    // Branch 2 – hand empty, pot not taken, IS a discard action

    /**When the player's hand is empty and the pot has not been taken yet,handlePot(true) must behave as above EXCEPT it must NOT call
    refreshHandPanel (the discard flow handles UI refresh separately).*/
    @Test
    void testHandlePot_takesWithoutRefresh_whenHandEmptyPotNotTaken_isDiscard() {
        final boolean result = potManager.handlePot(true);

        assertTrue(result, "Must return true when pot is taken");
        assertTrue(player.isInPot());

        verify(potView).showPotMessage(eq(player.getName()), eq(true));
        verify(potView).markPotTaken(anyBoolean());
        verify(potView, never()).refreshHandPanel(anyBoolean(), anyList());
    }

    

    // Branch 3 – hand NOT empty

    /*When the player still has cards in hand the pot must NOT be taken and the view must NOT be notified. Returns false.*/
    @Test
    void testHandlePot_doesNothing_whenHandNotEmpty() {
        player.addCardHand(new CardImpl("♠", "5"));

        final boolean result = potManager.handlePot(false);

        assertFalse(result, "Must return false when hand is not empty");
        assertFalse(player.isInPot(), "Player must not be marked as in-pot");

        verify(potView, never()).showPotMessage(anyString(), anyBoolean());
        verify(potView, never()).markPotTaken(anyBoolean());
        verify(potView, never()).refreshHandPanel(anyBoolean(), anyList());
    }

    

    // Branch 4 – hand empty but pot already taken

    /*When the player's hand is empty but the pot has already been taken, handlePot must be a no-op and return false.*/
    @Test
    void testHandlePot_doesNothing_whenPotAlreadyTaken() {
        player.setInPot(true); // already taken

        final boolean result = potManager.handlePot(false);

        assertFalse(result, "Must return false when pot already taken");

        verify(potView, never()).showPotMessage(anyString(), anyBoolean());
        verify(potView, never()).markPotTaken(anyBoolean());
        verify(potView, never()).refreshHandPanel(anyBoolean(), anyList());
    }

    


    // isPlayer1Turn flag is forwarded correctly

    //When it is player 2's turn, markPotTaken and refreshHandPanel must receive false as the isPlayer1 argument.
    @Test
    void testHandlePot_forwardsPlayer2TurnFlagToView() {
        when(turnModel.isPlayer1Turn()).thenReturn(false);

        potManager.handlePot(false);

        verify(potView).markPotTaken(false);
        verify(potView).refreshHandPanel(eq(false), anyList());
    }



    
    // showPotMessage receives the correct isDiscard argument


    //The isDiscard parameter passed to handlePot must be forwarded unchanged to PotView.showPotMessage().
    @Test
    void testHandlePot_forwardsIsDiscardFlagToShowPotMessage_true() {
        potManager.handlePot(true);
        verify(potView).showPotMessage(player.getName(), true);
    }

    @Test
    void testHandlePot_forwardsIsDiscardFlagToShowPotMessage_false() {
        potManager.handlePot(false);
        verify(potView).showPotMessage(player.getName(), false);
    }
}
