package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.jolly.JollyImpl;

class JollyTest {
    private static final String HEARTS = "♥";

    private CardImpl pureJolly;
    private CardImpl twoOfHearts;
    private CardImpl aceOfHearts;
    private CardImpl threeOfHearts;

    @BeforeEach
    void init() {
        this.pureJolly = new CardImpl("♕", "Jolly");
        this.twoOfHearts = new CardImpl(HEARTS, "2");
        this.aceOfHearts = new CardImpl(HEARTS, "A");
        this.threeOfHearts = new CardImpl(HEARTS, "3");
    }

    @Test
    void testIsPureJollyTrue() {
        JollyImpl jolly = new JollyImpl(pureJolly);
        assertTrue(jolly.isPureJolly());
    }

    @Test
    void testIsPureJollyFalseForTwo() {
        JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertFalse(jolly.isPureJolly());
    }

    @Test
    void testIsPureJollyFalseForNormalCard() {
        JollyImpl jolly = new JollyImpl(aceOfHearts);
        assertFalse(jolly.isPureJolly());
    }

    @Test
    void testPureJollyIsAlwaysJollyInAnyContext() {
        JollyImpl jolly = new JollyImpl(pureJolly);
        List<Card> context = List.of(aceOfHearts, threeOfHearts);
        assertTrue(jolly.isJolly(context));
    }

    @Test
    void testIsJollyReturnsFalseOnNullContext() {
        JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertFalse(jolly.isJolly(null));
    }

    @Test
    void testIsJollyReturnsFalseOnEmptyContext() {
        JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertFalse(jolly.isJolly(List.of()));
    }

    @Test
    void testTwoActsAsJollyWhenNotNaturalTwo() {
        JollyImpl jolly = new JollyImpl(twoOfHearts);
        CardImpl fiveOfHearts = new CardImpl(HEARTS, "5");
        CardImpl sevenOfHearts = new CardImpl(HEARTS, "7");
        List<Card> context = List.of(fiveOfHearts, twoOfHearts, sevenOfHearts);
        assertTrue(jolly.isJolly(context));
    }

    @Test
    void testTwoIsNotJollyWhenNaturalTwo() {
        JollyImpl jolly = new JollyImpl(twoOfHearts);
        List<Card> context = List.of(aceOfHearts, twoOfHearts, threeOfHearts);
        assertFalse(jolly.isJolly(context));
    }

    @Test
    void testNonTwoNonJollyCardIsNeverJolly() {
        JollyImpl jolly = new JollyImpl(aceOfHearts);
        List<Card> context = List.of(twoOfHearts, threeOfHearts);
        assertFalse(jolly.isJolly(context));
    }

    @Test
    void testGetCard() {
        JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertEquals(twoOfHearts, jolly.getCard());
    }
}
