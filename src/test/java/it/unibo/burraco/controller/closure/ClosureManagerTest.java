package it.unibo.burraco.controller.closure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.game.GameNotifier;


/**
 * Tests for {@link ClosureManager}:
 *  - handleStateAfterAction(): OK, ZERO_CARDS_NO_POT,
 *                              ZERO_CARDS_NO_BURRACO, CAN_CLOSE
 *  - handleStateAfterDiscard(): ROUND_WON, CANNOT_CLOSE_NO_BURRACO
 *  - attemptClosure(): delegates to handleStateAfterDiscard on currentPlayer
 */
class ClosureManagerTest {

    private static final int TARGET_SCORE = 2005;

    /** 7-card clean burraco used in multiple tests. */
    private static final List<Card> BURRACO = List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4"),new CardImpl("♥", "5"), new CardImpl("♥", "6"),new CardImpl("♥", "7"), new CardImpl("♥", "8"),new CardImpl("♥", "9"));

    private Turn turnModel;
    private GameNotifier notifier;
    private ScoreController scoreController;
    private ClosureManager manager;
    private Player player;

    @BeforeEach
    void setUp() {
        turnModel = mock(Turn.class);
        notifier = mock(GameNotifier.class);
        scoreController = mock(ScoreController.class);
        player = new PlayerImpl("Player1");
        
        manager = new ClosureManager(turnModel, notifier,TARGET_SCORE, scoreController);
    }



    // handleStateAfterAction()

    /* When the player still has cards in hand the state is OK:
    the method must return false and never trigger any notification or end.*/
    @Test
    void testHandleStateAfterAction_OK_whenHandNotEmpty() {
        player.addCardHand(new CardImpl("♠", "5"));

        final boolean result = manager.handleStateAfterAction(player);

        assertFalse(result, "OK state must return false");
        verify(turnModel, never()).setGameFinished(true);
        verify(notifier, never()).notifyMustTakePotBeforeDiscard();
        verify(notifier, never()).notifyMustFormBurracoBeforeClose();
        verify(scoreController, never()).onRoundEnd();
    }
    
    /* Hand empty AND pot not yet taken → ZERO_CARDS_NO_POT.
    The manager must notify the player and return true (action blocked).*/
    
    @Test
    void testHandleStateAfterAction_ZERO_CARDS_NO_POT_whenHandEmptyPotNotTaken() {
        // hand is empty by default in PlayerImpl; pot not taken

        final boolean result = manager.handleStateAfterAction(player);

        assertTrue(result, "ZERO_CARDS_NO_POT state must return true");
        verify(notifier).notifyMustTakePotBeforeDiscard();
        verify(turnModel, never()).setGameFinished(true);
    }
    
    /*Hand empty, pot taken, but no burraco yet → ZERO_CARDS_NO_BURRACO.
    the manager must notify the player and return true (action blocked).*/
    
    @Test
    void testHandleStateAfterAction_ZERO_CARDS_NO_BURRACO_whenPotTakenNoBurraco() {
        player.setInPot(true);

        final boolean result = manager.handleStateAfterAction(player);

        assertTrue(result, "ZERO_CARDS_NO_BURRACO state must return true");
        verify(notifier).notifyMustFormBurracoBeforeClose();
        verify(turnModel, never()).setGameFinished(true);
    }

    
    //Hand empty, pot taken, burraco present → CAN_CLOSE.The manager must trigger the round end and return true.
    @Test
    void testHandleStateAfterAction_CAN_CLOSE_triggersRoundEnd() {
        player.setInPot(true);
        player.addCombination(new ArrayList<>(BURRACO));

        final boolean result = manager.handleStateAfterAction(player);

        assertTrue(result, "CAN_CLOSE state must return true");
        verify(turnModel).setGameFinished(true);
        verify(scoreController).onRoundEnd();
    }

    /*CAN_CLOSE must NOT send any "must-take-pot" or "must-form-burraco"
    notifications before triggering the round end.*/
    @Test
    void testHandleStateAfterAction_CAN_CLOSE_doesNotNotifyWarnings() {
        player.setInPot(true);
        player.addCombination(new ArrayList<>(BURRACO));

        manager.handleStateAfterAction(player);

        verify(notifier, never()).notifyMustTakePotBeforeDiscard();
        verify(notifier, never()).notifyMustFormBurracoBeforeClose();
    }



    //handleStateAfterDiscard()


    //Hand empty, pot taken, burraco present → ROUND_WON.The manager must trigger the round end and return true.
    @Test
    void testHandleStateAfterDiscard_ROUND_WON_triggersRoundEnd() {
        player.setInPot(true);
        player.addCombination(new ArrayList<>(BURRACO));

        final boolean result = manager.handleStateAfterDiscard(player);

        assertTrue(result, "ROUND_WON state must return true");
        verify(turnModel).setGameFinished(true);
        verify(scoreController).onRoundEnd();
    }

    

    //hand empty, pot taken, NO burraco → CANNOT_CLOSE_NO_BURRACO.The manager must notify invalid closure and return false.
    @Test
    void testHandleStateAfterDiscard_CANNOT_CLOSE_NoBurraco_notifiesAndReturnsFalse() {
        player.setInPot(true);

        final boolean result = manager.handleStateAfterDiscard(player);

        assertFalse(result, "CANNOT_CLOSE_NO_BURRACO state must return false");
        verify(notifier).notifyInvalidClosure();
        verify(turnModel, never()).setGameFinished(true);
    }

    
    //Hand still has cards (and pot taken, burraco present) → OK state fromevaluateAfterDiscard → method returns false and does nothing special.
    @Test
    void testHandleStateAfterDiscard_OK_whenHandNotEmpty() {
        player.setInPot(true);
        player.addCombination(new ArrayList<>(BURRACO));
        player.addCardHand(new CardImpl("♠", "K")); // still 1 card left

        final boolean result = manager.handleStateAfterDiscard(player);

        assertFalse(result, "OK state must return false");
        verify(turnModel, never()).setGameFinished(true);
        verify(notifier, never()).notifyInvalidClosure();
    }

    
    // attemptClosure()

    /*attemptClosure() uses turnModel.getCurrentPlayer() and delegates to
    handleStateAfterDiscard. When the current player can win it musttrigger the round end.*/
    
    @Test
    void testAttemptClosure_triggersRoundEnd_whenCurrentPlayerCanClose() {
        // Arrange: the current player returned by the mock can win
        final Player closingPlayer = new PlayerImpl("Closer");
        closingPlayer.setInPot(true);
        closingPlayer.addCombination(new ArrayList<>(BURRACO));

        when(turnModel.getCurrentPlayer()).thenReturn(closingPlayer);

        manager.attemptClosure();

        verify(turnModel).setGameFinished(true);
        verify(scoreController).onRoundEnd();
    }


    /*attemptClosure() when the current player cannot close (no burraco):must notify and NOT end the round.*/
    @Test
    void testAttemptClosure_notifiesInvalidClosure_whenNoBurraco() {
        final Player noBurracoPlayer = new PlayerImpl("NoBurraco");
        noBurracoPlayer.setInPot(true); // pot taken but 0 burracos, hand empty

        when(turnModel.getCurrentPlayer()).thenReturn(noBurracoPlayer);

        manager.attemptClosure();

        verify(notifier).notifyInvalidClosure();
        verify(turnModel, never()).setGameFinished(true);
    }
}

