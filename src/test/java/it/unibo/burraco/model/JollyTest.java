package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.JollyImpl;

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
    void testPureJollyIdentification() {
        final JollyImpl jolly = new JollyImpl(this.pureJolly);
        assertTrue(jolly.isPureJolly());

        final JollyImpl notAJolly = new JollyImpl(this.twoOfHearts);
        assertFalse(notAJolly.isPureJolly());
    }

    @Test
    void testPureJollyIsAlwaysJollyInAnyContext() {
        final JollyImpl jolly = new JollyImpl(this.pureJolly);
        final List<Card> context = List.of(this.aceOfHearts, this.threeOfHearts);
        assertTrue(jolly.isJolly(context));
    }

    @Test
    void testIsJollyEdgeCases() {
        final JollyImpl jolly = new JollyImpl(this.twoOfHearts);
        assertFalse(jolly.isJolly(null));
        assertFalse(jolly.isJolly(List.of()));
    }

    @Test
    void testTwoActsAsJollyWhenNotNaturalTwo() {
        final JollyImpl jolly = new JollyImpl(this.twoOfHearts);
        final CardImpl fiveOfHearts = new CardImpl(HEARTS, "5");
        final CardImpl sevenOfHearts = new CardImpl(HEARTS, "7");

        final List<Card> context = List.of(fiveOfHearts, this.twoOfHearts, sevenOfHearts);
        assertTrue(jolly.isJolly(context), "The '2' should act as a wildcard here.");
    }

    @Test
    void testTwoIsNotJollyWhenNaturalTwo() {
        final JollyImpl jolly = new JollyImpl(this.twoOfHearts);

        final List<Card> context = List.of(this.aceOfHearts, this.twoOfHearts, this.threeOfHearts);
        assertFalse(jolly.isJolly(context));
    }

    @Test
    void testNormalCardIsNeverJolly() {
        final JollyImpl notAJolly = new JollyImpl(this.aceOfHearts);
        final List<Card> context = List.of(this.twoOfHearts, this.threeOfHearts);
        assertFalse(notAJolly.isJolly(context));
    }

    @Test
    void testGetCard() {
        final JollyImpl jolly = new JollyImpl(this.twoOfHearts);
        assertEquals(this.twoOfHearts, jolly.getCard());
    }
}
