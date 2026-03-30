package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.TurnImpl;

class TurnTest {
    private static final String NAME_P1 = "Alice";
    private static final String NAME_P2 = "Bob";

    private PlayerImpl player1;
    private PlayerImpl player2;
    private TurnImpl turn;

    @BeforeEach
    void init() {
        this.player1 = new PlayerImpl(NAME_P1);
        this.player2 = new PlayerImpl(NAME_P2);
        this.turn = new TurnImpl(player1, player2);
    }

    @Test
    void testInitialTurnIsPlayer1() {
        assertTrue(turn.isPlayer1Turn());
        assertEquals(player1, turn.getCurrentPlayer());
    }

    @Test
    void testNextTurnSwitchesToPlayer2() {
        turn.nextTurn();
        assertFalse(turn.isPlayer1Turn());
        assertEquals(player2, turn.getCurrentPlayer());
    }

    @Test
    void testNextTurnSwitchesBack() {
        turn.nextTurn();
        turn.nextTurn();
        assertTrue(turn.isPlayer1Turn());
        assertEquals(player1, turn.getCurrentPlayer());
    }

    @Test
    void testGetPlayer1() {
        assertEquals(player1, turn.getPlayer1());
    }

    @Test
    void testGetPlayer2() {
        assertEquals(player2, turn.getPlayer2());
    }

    @Test
    void testInitialGameNotFinished() {
        assertFalse(turn.isGameFinished());
    }

    @Test
    void testSetGameFinished() {
        turn.setGameFinished(true);
        assertTrue(turn.isGameFinished());
        turn.setGameFinished(false);
        assertFalse(turn.isGameFinished());
    }

    @Test
    void testCanCloseReturnsFalseByDefault() {
        assertFalse(turn.canClose());
    }

    @Test
    void testCanCloseReturnsFalseWithPotButNoBurraco() {
        player1.setInPot(true);
        assertFalse(turn.canClose());
    }

    @Test
    void testCanCloseReturnsFalseWithBurracoButNoPot() {
        final List<Card> burraco = List.of(
            new CardImpl("♥", "3"), new CardImpl("♥", "4"),
            new CardImpl("♥", "5"), new CardImpl("♥", "6"),
            new CardImpl("♥", "7"), new CardImpl("♥", "8"),
            new CardImpl("♥", "9")
        );
        player1.addCombination(burraco);
        assertFalse(turn.canClose());
    }

    @Test
    void testCanCloseReturnsTrueWithPotAndBurraco() {
        player1.setInPot(true);
        final List<Card> burraco = List.of(
            new CardImpl("♥", "3"), new CardImpl("♥", "4"),
            new CardImpl("♥", "5"), new CardImpl("♥", "6"),
            new CardImpl("♥", "7"), new CardImpl("♥", "8"),
            new CardImpl("♥", "9")
        );
        player1.addCombination(burraco);
        assertTrue(turn.canClose());
    }

    @Test
    void testCanCloseChecksCurrentPlayer() {
        turn.nextTurn();
        player2.setInPot(true);
        final List<Card> burraco = List.of(
            new CardImpl("♠", "3"), new CardImpl("♠", "4"),
            new CardImpl("♠", "5"), new CardImpl("♠", "6"),
            new CardImpl("♠", "7"), new CardImpl("♠", "8"),
            new CardImpl("♠", "9")
        );
        player2.addCombination(burraco);
        assertTrue(turn.canClose());
    }

    @Test
    void testResetForNewRound() {
        turn.nextTurn();
        turn.setGameFinished(true);
        turn.resetForNewRound();
        assertTrue(turn.isPlayer1Turn());
        assertFalse(turn.isGameFinished());
    }
}
