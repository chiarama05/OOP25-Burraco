package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.score.CardPoint;

class CardPointTest {
    private static final String HEARTS = "♥";
    private static final String JOLLY_SEED = "♕";

    @Test
    void testJollyWorth30() {
        final Card jolly = new CardImpl(JOLLY_SEED, "Jolly");
        assertEquals(30, CardPoint.getCardPoints(jolly));
    }

    @Test
    void testTwoWorth20() {
        final Card two = new CardImpl(HEARTS, "2");
        assertEquals(20, CardPoint.getCardPoints(two));
    }

    @Test
    void testAceWorth15() {
        final Card ace = new CardImpl(HEARTS, "A");
        assertEquals(15, CardPoint.getCardPoints(ace));
    }

    @Test
    void testHighCardsWorth10() {
        final String[] highValues = {"K", "Q", "J", "10", "9", "8"};
        for (final String value : highValues) {
            final Card card = new CardImpl(HEARTS, value);
            assertEquals(10, CardPoint.getCardPoints(card), "Expected 10 for value: " + value);
        }
    }

    @Test
    void testLowCardsWorth5() {
        final String[] lowValues = {"7", "6", "5", "4", "3"};
        for (final String value : lowValues) {
            final Card card = new CardImpl(HEARTS, value);
            assertEquals(5, CardPoint.getCardPoints(card), "Expected 5 for value: " + value);
        }
    }

    @Test
    void testUnknownValueWorth0() {
        final Card unknown = new CardImpl(HEARTS, "InvalidValue");
        assertEquals(0, CardPoint.getCardPoints(unknown));
    }

    @Test
    void testToIntMapping() {
        final String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        final int[]    expected = {  1,  2,  3,  4,  5,  6,  7,  8,  9,  10, 11, 12, 13};

        for (int i = 0; i < values.length; i++) {
            final Card card = new CardImpl(HEARTS, values[i]);
            assertEquals(expected[i], CardPoint.toInt(card), "Expected " + expected[i] + " for value: " + values[i]);
        }
    }

    @Test
    void testToIntThrowsOnJolly() {
        final Card jolly = new CardImpl(JOLLY_SEED, "Jolly");
        assertThrows(IllegalArgumentException.class, () -> CardPoint.toInt(jolly));
    }

    @Test
    void testToIntThrowsOnUnknownValue() {
        final Card unknown = new CardImpl(HEARTS, "InvalidValue");
        assertThrows(IllegalArgumentException.class, () -> CardPoint.toInt(unknown));
    }

    @Test
    void testPrivateConstructor() {
        final var constructors = CardPoint.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertFalse(constructors[0].canAccess(null));
    }
}
