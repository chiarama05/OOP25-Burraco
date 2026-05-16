package it.unibo.burraco.model.turn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;
import it.unibo.burraco.model.player.PlayerImpl;

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
        this.turn = new TurnImpl(this.player1, this.player2);
    }

    @Test
    void testInitialTurnIsPlayer1() {
        assertTrue(this.turn.isPlayer1Turn());
        assertEquals(this.player1, this.turn.getCurrentPlayer());
    }

    @Test
    void testNextTurnSwitchesToPlayer2() {
        this.turn.nextTurn();
        assertFalse(this.turn.isPlayer1Turn());
        assertEquals(this.player2, this.turn.getCurrentPlayer());
    }

    @Test
    void testNextTurnSwitchesBack() {
        this.turn.nextTurn();
        this.turn.nextTurn();
        assertTrue(this.turn.isPlayer1Turn());
        assertEquals(this.player1, this.turn.getCurrentPlayer());
    }

    @Test
    void testGetPlayers() {
        assertEquals(this.player1, this.turn.getPlayer1());
        assertEquals(this.player2, this.turn.getPlayer2());
    }

    @Test
    void testInitialGameNotFinished() {
        assertFalse(this.turn.isGameFinished());
    }

    @Test
    void testSetGameFinished() {
        this.turn.setGameFinished(true);
        assertTrue(this.turn.isGameFinished());
        this.turn.setGameFinished(false);
        assertFalse(this.turn.isGameFinished());
    }

    @Test
    void testCanCloseConditions() {
        final List<Card> burraco = List.of(
            new CardImpl(Seed.HEARTS, CardValue.THREE), new CardImpl(Seed.HEARTS, CardValue.FOUR),
            new CardImpl(Seed.HEARTS, CardValue.FIVE), new CardImpl(Seed.HEARTS, CardValue.SIX),
            new CardImpl(Seed.HEARTS, CardValue.SEVEN), new CardImpl(Seed.HEARTS, CardValue.EIGHT),
            new CardImpl(Seed.HEARTS, CardValue.NINE)
        );

        assertFalse(this.turn.canClose());

        this.player1.setInPot(true);
        assertFalse(this.turn.canClose());

        this.player1.setInPot(false);
        this.player1.addCombination(burraco);
        assertFalse(this.turn.canClose());

        this.player1.setInPot(true);
        assertTrue(this.turn.canClose());
    }

    @Test
    void testCanCloseChecksCurrentPlayer() {
        this.turn.nextTurn();
        this.player2.setInPot(true);
        final List<Card> burraco = List.of(
            new CardImpl(Seed.SPADES, CardValue.THREE), new CardImpl(Seed.SPADES, CardValue.FOUR),
            new CardImpl(Seed.SPADES, CardValue.FIVE), new CardImpl(Seed.SPADES, CardValue.SIX),
            new CardImpl(Seed.SPADES, CardValue.SEVEN), new CardImpl(Seed.SPADES, CardValue.EIGHT),
            new CardImpl(Seed.SPADES, CardValue.NINE)
        );
        this.player2.addCombination(burraco);
        assertTrue(this.turn.canClose());
    }

    @Test
    void testResetForNewRound() {
        this.turn.nextTurn();
        this.turn.setGameFinished(true);
        this.turn.resetForNewRound();
        assertTrue(this.turn.isPlayer1Turn());
        assertFalse(this.turn.isGameFinished());
    }
}
