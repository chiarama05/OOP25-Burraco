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
        this.player1 = new PlayerImpl("Alice");
        this.player2 = new PlayerImpl("Bob");
        this.turnModel = new TurnImpl(this.player1, this.player2);
        this.drawManager = new DrawManager();
        this.turnController = new TurnController(this.turnModel, this.drawManager);
    }

    @Test
    void testInitiallyPlayer1Turn() {
        assertTrue(this.turnModel.isPlayer1Turn());
        assertEquals(this.player1, this.turnModel.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnSwitchesToPlayer2() {
        this.turnController.executeNextTurn();
        assertFalse(this.turnModel.isPlayer1Turn());
        assertEquals(this.player2, this.turnModel.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnSwitchesBackToPlayer1() {
        this.turnController.executeNextTurn();
        this.turnController.executeNextTurn();
        assertTrue(this.turnModel.isPlayer1Turn());
        assertEquals(this.player1, this.turnModel.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnResetsDrawManager() {
        this.drawManager.drawFromDeck(this.player1, new it.unibo.burraco.model.deck.DeckImpl());
        assertTrue(this.drawManager.hasDrawn());
        this.turnController.executeNextTurn();
        assertFalse(this.drawManager.hasDrawn());
    }

    @Test
    void testListenerIsCalledOnExecuteNextTurn() {
        final AtomicBoolean fired = new AtomicBoolean(false);
        this.turnController.setOnTurnChangedListener(() -> fired.set(true));
        this.turnController.executeNextTurn();
        assertTrue(fired.get(), "Listener must be called after executeNextTurn()");
    }

    @Test
    void testListenerNotificationCounts() {
        final AtomicInteger count = new AtomicInteger(0);
        this.turnController.setOnTurnChangedListener(count::incrementAndGet);

        this.turnController.executeNextTurn();
        assertEquals(1, count.get());

        this.turnController.executeNextTurn();
        assertEquals(2, count.get());
    }

    @Test
    void testReplacingListenerCallsOnlyNewOne() {
        final AtomicBoolean firstCalled  = new AtomicBoolean(false);
        final AtomicBoolean secondCalled = new AtomicBoolean(false);

        this.turnController.setOnTurnChangedListener(() -> firstCalled.set(true));
        this.turnController.setOnTurnChangedListener(() -> secondCalled.set(true));
        this.turnController.executeNextTurn();

        assertFalse(firstCalled.get(), "Old listener must NOT be called");
        assertTrue(secondCalled.get(), "New listener must be called");
    }

    @Test
    void testNoListenerDoesNotThrow() {
        assertDoesNotThrow(() -> this.turnController.executeNextTurn());
    }

    @Test
    void testNullDrawManagerDoesNotThrow() {
        final TurnController ctrl = new TurnController(this.turnModel, null);
        assertDoesNotThrow(() -> ctrl.executeNextTurn());
    }

    @Test
    void testGameFinishedFlagPersistence() {
        this.turnModel.setGameFinished(true);
        this.turnController.executeNextTurn();
        assertTrue(this.turnModel.isGameFinished());
    }

    @Test
    void testCanCloseReflectsCurrentPlayerAfterTurnChange() {
        this.player2.setInPot(true);
        this.player2.addCombination(BURRACO);

        assertFalse(this.turnModel.canClose());
        this.turnController.executeNextTurn();
        assertTrue(this.turnModel.canClose());
    }
}
