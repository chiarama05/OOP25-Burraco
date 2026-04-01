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

class ClosureManagerTest {

    private static final int TARGET_SCORE = 2005;
    private static final List<Card> BURRACO = List.of(
        new CardImpl("♥", "3"), new CardImpl("♥", "4"), new CardImpl("♥", "5"),
        new CardImpl("♥", "6"), new CardImpl("♥", "7"), new CardImpl("♥", "8"),
        new CardImpl("♥", "9")
    );

    private Turn turnModel;
    private GameNotifier notifier;
    private ScoreController scoreController;
    private Player player;
    private ClosureManager manager;

    @BeforeEach
    void setUp() {
        this.turnModel = mock(Turn.class);
        this.notifier = mock(GameNotifier.class);
        this.scoreController = mock(ScoreController.class);
        this.player = new PlayerImpl("Player1");

        this.manager = new ClosureManager(this.turnModel, this.notifier, TARGET_SCORE, this.scoreController);
    }

    @Test
    void testHandleStateAfterActionOkWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl("♠", "5"));

        final boolean result = this.manager.handleStateAfterAction(this.player);

        assertFalse(result, "OK state must return false");
        verify(this.turnModel, never()).setGameFinished(true);
        verify(this.notifier, never()).notifyMustTakePotBeforeDiscard();
        verify(this.notifier, never()).notifyMustFormBurracoBeforeClose();
        verify(this.scoreController, never()).onRoundEnd();
    }

    @Test
    void testHandleStateAfterActionNoPotWhenHandEmpty() {
        final boolean result = this.manager.handleStateAfterAction(this.player);

        assertTrue(result, "ZERO_CARDS_NO_POT state must return true");
        verify(this.notifier).notifyMustTakePotBeforeDiscard();
        verify(this.turnModel, never()).setGameFinished(true);
    }

    @Test
    void testHandleStateAfterActionNoBurracoWhenPotTaken() {
        this.player.setInPot(true);

        final boolean result = this.manager.handleStateAfterAction(this.player);

        assertTrue(result, "ZERO_CARDS_NO_BURRACO state must return true");
        verify(this.notifier).notifyMustFormBurracoBeforeClose();
        verify(this.turnModel, never()).setGameFinished(true);
    }

    @Test
    void testHandleStateAfterActionCanCloseTriggersRoundEnd() {
        this.player.setInPot(true);
        this.player.addCombination(new ArrayList<>(BURRACO));

        final boolean result = this.manager.handleStateAfterAction(this.player);

        assertTrue(result, "CAN_CLOSE state must return true");
        verify(this.turnModel).setGameFinished(true);
        verify(this.scoreController).onRoundEnd();
    }

    @Test
    void testHandleStateAfterActionCanCloseDoesNotNotifyWarnings() {
        this.player.setInPot(true);
        this.player.addCombination(new ArrayList<>(BURRACO));

        this.manager.handleStateAfterAction(this.player);

        verify(this.notifier, never()).notifyMustTakePotBeforeDiscard();
        verify(this.notifier, never()).notifyMustFormBurracoBeforeClose();
    }

    @Test
    void testHandleStateAfterDiscardRoundWonTriggersRoundEnd() {
        this.player.setInPot(true);
        this.player.addCombination(new ArrayList<>(BURRACO));

        final boolean result = this.manager.handleStateAfterDiscard(this.player);

        assertTrue(result, "ROUND_WON state must return true");
        verify(this.turnModel).setGameFinished(true);
        verify(this.scoreController).onRoundEnd();
    }

    @Test
    void testHandleStateAfterDiscardCannotCloseNoBurraco() {
        this.player.setInPot(true);

        final boolean result = this.manager.handleStateAfterDiscard(this.player);

        assertFalse(result, "CANNOT_CLOSE_NO_BURRACO state must return false");
        verify(this.notifier).notifyInvalidClosure();
        verify(this.turnModel, never()).setGameFinished(true);
    }

    @Test
    void testHandleStateAfterDiscardOkWhenHandNotEmpty() {
        this.player.setInPot(true);
        this.player.addCombination(new ArrayList<>(BURRACO));
        this.player.addCardHand(new CardImpl("♠", "K"));

        final boolean result = this.manager.handleStateAfterDiscard(this.player);

        assertFalse(result, "OK state must return false");
        verify(this.turnModel, never()).setGameFinished(true);
        verify(this.notifier, never()).notifyInvalidClosure();
    }

    @Test
    void testAttemptClosureTriggersRoundEndWhenCurrentPlayerCanClose() {
        final Player closingPlayer = new PlayerImpl("Closer");
        closingPlayer.setInPot(true);
        closingPlayer.addCombination(new ArrayList<>(BURRACO));

        when(this.turnModel.getCurrentPlayer()).thenReturn(closingPlayer);

        this.manager.attemptClosure();

        verify(this.turnModel).setGameFinished(true);
        verify(this.scoreController).onRoundEnd();
    }

    @Test
    void testAttemptClosureNotifiesInvalidClosureWhenNoBurraco() {
        final Player noBurracoPlayer = new PlayerImpl("NoBurraco");
        noBurracoPlayer.setInPot(true);

        when(this.turnModel.getCurrentPlayer()).thenReturn(noBurracoPlayer);

        this.manager.attemptClosure();

        verify(this.notifier).notifyInvalidClosure();
        verify(this.turnModel, never()).setGameFinished(true);
    }
}
