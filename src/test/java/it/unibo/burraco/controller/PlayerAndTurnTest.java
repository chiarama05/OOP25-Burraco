package it.unibo.burraco.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.TurnImpl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/*
Players and turns managements
PlayerImpl (hand, combinations,score, reset)
TurnImpl (turn's changes, canClose, gameFinished, reset)
TurnController (executeNextTurn, listener)
 */
public class PlayerAndTurnTest {

    private PlayerImpl p1, p2;
    private TurnImpl turn;

    private static final List<Card> BURRACO = List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4"),new CardImpl("♥", "5"), new CardImpl("♥", "6"),new CardImpl("♥", "7"), new CardImpl("♥", "8"),new CardImpl("♥", "9"));

    @BeforeEach
    void setUp() {
        p1= new PlayerImpl("Alice");
        p2= new PlayerImpl("Bob");
        turn= new TurnImpl(p1, p2);
    }
 
    // Player - ctors and initial state

    @Test
    void testNamedConstructor() {
        assertEquals("Alice", p1.getName());
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("Player", new PlayerImpl().getName());
    }

    @Test
    void testInitialHandEmpty() {
        assertTrue(p1.getHand().isEmpty());
    }

    @Test
    void testInitialCombinationsEmpty() {
        assertTrue(p1.getCombinations().isEmpty());
    }

    @Test
    void testInitialNotInPot() {
        assertFalse(p1.isInPot());
    }

    @Test
    void testInitialMatchScoreZero() {
        assertEquals(0, p1.getMatchTotalScore());
    }

    @Test
    void testInitialHasFinishedCards() {
        assertTrue(p1.hasFinishedCards());
    }

    // Hand

    @Test
    void testAddCardHand() {
        Card c = new CardImpl("♠", "A");
        p1.addCardHand(c);
        assertEquals(1, p1.getHand().size());
        assertTrue(p1.hasCard(c));
    }

    @Test
    void testRemoveCardHand() {
        Card c = new CardImpl("♠", "A");
        p1.addCardHand(c);
        p1.removeCardHand(c);
        assertFalse(p1.hasCard(c));
        assertTrue(p1.getHand().isEmpty());
    }

    @Test
    void testRemoveCards() {
        Card c1 = new CardImpl("♠", "A");
        Card c2 = new CardImpl("♠", "K");
        Card c3 = new CardImpl("♠", "Q");
        p1.addCardHand(c1); p1.addCardHand(c2); p1.addCardHand(c3);
        p1.removeCards(List.of(c1, c3));
        assertEquals(1, p1.getHand().size());
        assertTrue(p1.hasCard(c2));
        assertFalse(p1.hasCard(c1));
    }

    @Test
    void testHasFinishedCardsFalseWhenNotEmpty() {
        p1.addCardHand(new CardImpl("♦", "5"));
        assertFalse(p1.hasFinishedCards());
    }

    //Combination's

    @Test
    void testAddCombination() {
        p1.addCombination(BURRACO);
        assertEquals(1, p1.getCombinations().size());
    }

    @Test
    void testBurracoCountZeroNoCombo() {
        assertEquals(0, p1.getBurracoCount());
    }

    @Test
    void testBurracoCountOneWith7Cards() {
        p1.addCombination(BURRACO);
        assertEquals(1, p1.getBurracoCount());
    }

    @Test
    void testBurracoCountZeroWithShortCombo() {
        p1.addCombination(List.of(new CardImpl("♠", "5"), new CardImpl("♠", "6"),new CardImpl("♠", "7")));
        assertEquals(0, p1.getBurracoCount());
    }

    @Test
    void testMultipleBurraci() {
        p1.addCombination(BURRACO);
        p1.addCombination(List.of(new CardImpl("♦", "3"), new CardImpl("♦", "4"),new CardImpl("♦", "5"), new CardImpl("♦", "6"),new CardImpl("♦", "7"), new CardImpl("♦", "8"),new CardImpl("♦", "9")));
        assertEquals(2, p1.getBurracoCount());
    }

    //Game's score
    @Test
    void testAddPointsAccumulates() {
        p1.addPointsToMatch(200);
        p1.addPointsToMatch(150);
        assertEquals(350, p1.getMatchTotalScore());
    }

    @Test
    void testResetDoesNotClearMatchScore() {
        p1.addPointsToMatch(500);
        p1.resetForNewRound();
        assertEquals(500, p1.getMatchTotalScore());
    }

    //Reset

    @Test
    void testResetClearsHandCombinationsPotFlag() {
        p1.addCardHand(new CardImpl("♠", "A"));
        p1.addCombination(BURRACO);
        p1.setInPot(true);
        p1.resetForNewRound();
        assertTrue(p1.getHand().isEmpty());
        assertTrue(p1.getCombinations().isEmpty());
        assertFalse(p1.isInPot());
    }

    
    // Turn
    @Test
    void testInitialTurnIsPlayer1() {
        assertTrue(turn.isPlayer1Turn());
        assertEquals(p1, turn.getCurrentPlayer());
    }

    @Test
    void testNextTurnSwitchesToPlayer2() {
        turn.nextTurn();
        assertFalse(turn.isPlayer1Turn());
        assertEquals(p2, turn.getCurrentPlayer());
    }

    @Test
    void testNextTurnSwitchesBackToPlayer1() {
        turn.nextTurn(); turn.nextTurn();
        assertTrue(turn.isPlayer1Turn());
    }

    @Test
    void testGetPlayer1AndPlayer2() {
        assertEquals(p1, turn.getPlayer1());
        assertEquals(p2, turn.getPlayer2());
    }

    //canClose()

    @Test
    void testCanCloseDefaultFalse() {
        assertFalse(turn.canClose());
    }

    @Test
    void testCanCloseFalsePotNoBurraco() {
        p1.setInPot(true);
        assertFalse(turn.canClose());
    }

    @Test
    void testCanCloseFalseBurracoNoPot() {
        p1.addCombination(BURRACO);
        assertFalse(turn.canClose());
    }

    @Test
    void testCanCloseTrueWithBothConditions() {
        p1.setInPot(true);
        p1.addCombination(BURRACO);
        assertTrue(turn.canClose());
    }

    @Test
    void testCanCloseChecksCurrentPlayer() {
        p2.setInPot(true);
        p2.addCombination(BURRACO);
        assertFalse(turn.canClose()); //p1's turn
        turn.nextTurn();
        assertTrue(turn.canClose());  //p2's turn
    }

    //gameFinished 

    @Test
    void testGameNotFinishedInitially() {
        assertFalse(turn.isGameFinished());
    }

    @Test
    void testSetGameFinished() {
        turn.setGameFinished(true);
        assertTrue(turn.isGameFinished());
        turn.setGameFinished(false);
        assertFalse(turn.isGameFinished());
    }

    //Turn reset 
    @Test
    void testTurnResetRestoresPlayer1AndNotFinished() {
        turn.nextTurn();
        turn.setGameFinished(true);
        turn.resetForNewRound();
        assertTrue(turn.isPlayer1Turn());
        assertFalse(turn.isGameFinished());
    }

    
    // TurnController

    @Test
    void testExecuteNextTurnSwitchesPlayer() {
        TurnController ctrl = new TurnController(turn, null);
        ctrl.executeNextTurn();
        assertEquals(p2, turn.getCurrentPlayer());
    }

    @Test
    void testExecuteNextTurnFiresListener() {
        AtomicBoolean fired = new AtomicBoolean(false);
        TurnController ctrl = new TurnController(turn, null);
        ctrl.setOnTurnChangedListener(() -> fired.set(true));
        ctrl.executeNextTurn();
        assertTrue(fired.get());
    }

    @Test
    void testExecuteNextTurnWithNoListenerNoException() {
        TurnController ctrl = new TurnController(turn, null);
        assertDoesNotThrow(ctrl::executeNextTurn);
    }

    @Test
    void testMultipleExecuteNextTurnAlternates() {
        TurnController ctrl = new TurnController(turn, null);
        ctrl.executeNextTurn();
        ctrl.executeNextTurn();
        ctrl.executeNextTurn();
        assertEquals(p2, turn.getCurrentPlayer());
    }
}
