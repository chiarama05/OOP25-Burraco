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
        final JollyImpl jolly = new JollyImpl(pureJolly);
        assertTrue(jolly.isPureJolly());
    }

    @Test
    void testIsPureJollyFalseForTwo() {
        final JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertFalse(jolly.isPureJolly());
    }

    @Test
    void testIsPureJollyFalseForNormalCard() {
        final JollyImpl jolly = new JollyImpl(aceOfHearts);
        assertFalse(jolly.isPureJolly());
    }

    @Test
    void testPureJollyIsAlwaysJollyInAnyContext() {
        final JollyImpl jolly = new JollyImpl(pureJolly);
        final List<Card> context = List.of(aceOfHearts, threeOfHearts);
        assertTrue(jolly.isJolly(context));
    }

    @Test
    void testIsJollyReturnsFalseOnNullContext() {
        final JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertFalse(jolly.isJolly(null));
    }

    @Test
    void testIsJollyReturnsFalseOnEmptyContext() {
        final JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertFalse(jolly.isJolly(List.of()));
    }

    @Test
    void testTwoActsAsJollyWhenNotNaturalTwo() {
        final JollyImpl jolly = new JollyImpl(twoOfHearts);
        final CardImpl fiveOfHearts = new CardImpl(HEARTS, "5");
        final CardImpl sevenOfHearts = new CardImpl(HEARTS, "7");
        final List<Card> context = List.of(fiveOfHearts, twoOfHearts, sevenOfHearts);
        assertTrue(jolly.isJolly(context));
    }

    @Test
    void testTwoIsNotJollyWhenNaturalTwo() {
        final JollyImpl jolly = new JollyImpl(twoOfHearts);
        final List<Card> context = List.of(aceOfHearts, twoOfHearts, threeOfHearts);
        assertFalse(jolly.isJolly(context));
    }

    @Test
    void testNonTwoNonJollyCardIsNeverJolly() {
        final JollyImpl jolly = new JollyImpl(aceOfHearts);
        final List<Card> context = List.of(twoOfHearts, threeOfHearts);
        assertFalse(jolly.isJolly(context));
    }

    @Test
    void testGetCard() {
        final JollyImpl jolly = new JollyImpl(twoOfHearts);
        assertEquals(twoOfHearts, jolly.getCard());
    }
}
