package it.unibo.burraco.controller.turn;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.turn.TurnImpl;
 
class TurnControllerTest {

    private Player player1;
    private Player player2;
    private Turn turnModel;
    private DrawManager drawManager;
    private TurnController turnController;

    private static final List<Card> BURRACO = List.of(
        new CardImpl("♥", "3"),
        new CardImpl("♥", "4"),
        new CardImpl("♥", "5"),
        new CardImpl("♥", "6"),
        new CardImpl("♥", "7"),
        new CardImpl("♥", "8"),
        new CardImpl("♥", "9")
    );

    @BeforeEach
    void setUp() {
        player1 = new PlayerImpl("Alice");
        player2 = new PlayerImpl("Bob");
        turnModel = new TurnImpl(player1, player2);
        drawManager = new DrawManager();
        turnController = new TurnController(turnModel, drawManager);
    }

    @Test
    void testInitiallyPlayer1Turn() {
        assertTrue(turnModel.isPlayer1Turn());
        assertEquals(player1, turnModel.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnSwitchesToPlayer2() {
        turnController.executeNextTurn();
        assertFalse(turnModel.isPlayer1Turn());
        assertEquals(player2, turnModel.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnSwitchesBackToPlayer1() {
        turnController.executeNextTurn();
        turnController.executeNextTurn();
        assertTrue(turnModel.isPlayer1Turn());
        assertEquals(player1, turnModel.getCurrentPlayer());
    }

    @Test
    void testThreeExecuteNextTurnEndsOnPlayer2() {
        turnController.executeNextTurn();
        turnController.executeNextTurn();
        turnController.executeNextTurn();
        assertEquals(player2, turnModel.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnResetsDrawManager() {
        drawManager.drawFromDeck(player1,new it.unibo.burraco.model.deck.DeckImpl());
        assertTrue(drawManager.hasDrawn());
        turnController.executeNextTurn();
        assertFalse(drawManager.hasDrawn(),"DrawManager must be reset after executeNextTurn()");
    }

    @Test
    void testListenerIsCalledOnExecuteNextTurn() {
        final AtomicBoolean fired = new AtomicBoolean(false);
        turnController.setOnTurnChangedListener(() -> fired.set(true));
        turnController.executeNextTurn();
        assertTrue(fired.get(), "Listener must be called after executeNextTurn()");
    }

    @Test
    void testListenerIsCalledOncePerExecution() {
        final AtomicInteger count = new AtomicInteger(0);
        turnController.setOnTurnChangedListener(count::incrementAndGet);
        turnController.executeNextTurn();
        assertEquals(1, count.get());
    }

    @Test
    void testListenerIsCalledTwiceForTwoExecutions() {
        final AtomicInteger count = new AtomicInteger(0);
        turnController.setOnTurnChangedListener(count::incrementAndGet);
        turnController.executeNextTurn();
        turnController.executeNextTurn();
        assertEquals(2, count.get());
    }

    @Test
    void testReplacingListenerCallsNewOne() {
        final AtomicBoolean firstCalled  = new AtomicBoolean(false);
        final AtomicBoolean secondCalled = new AtomicBoolean(false);
        turnController.setOnTurnChangedListener(() -> firstCalled.set(true));
        turnController.setOnTurnChangedListener(() -> secondCalled.set(true));
        turnController.executeNextTurn();
        assertFalse(firstCalled.get(),  "Old listener must NOT be called");
        assertTrue(secondCalled.get(), "New listener must be called");
    }

    @Test
    void testNoListenerDoesNotThrow() {
        assertDoesNotThrow(turnController::executeNextTurn);
    }

    @Test
    void testNullDrawManagerDoesNotThrow() {
        final TurnController ctrl = new TurnController(turnModel, null);
        assertDoesNotThrow(ctrl::executeNextTurn);
    }

    @Test
    void testGameFinishedFlagNotClearedByExecuteNextTurn() {
        turnModel.setGameFinished(true);
        turnController.executeNextTurn();
        assertTrue(turnModel.isGameFinished(),"executeNextTurn() must not clear the gameFinished flag");
    }

    @Test
    void testCanCloseReflectsCurrentPlayerAfterTurnChange() {
        player2.setInPot(true);
        player2.addCombination(BURRACO);
        assertFalse(turnModel.canClose());
        turnController.executeNextTurn();
        assertTrue(turnModel.canClose());
    }
}
