package it.unibo.burraco.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;

class CardPointTest {
    private static final int JOLLY_POINTS = 30;
    private static final int TWO_POINTS = 20;
    private static final int ACE_POINTS = 15;
    private static final int HIGH_CARD_POINTS = 10;
    private static final int LOW_CARD_POINTS = 5;
    private static final int CONSTRUCTOR_COUNT = 1;

    @Test
    void testJollyWorth30() {
        final Card jolly = new CardImpl(Seed.JOKER, CardValue.JOLLY);
        assertEquals(JOLLY_POINTS, CardPoint.getCardPoints(jolly));
    }

    @Test
    void testTwoWorth20() {
        final Card two = new CardImpl(Seed.HEARTS, CardValue.TWO);
        assertEquals(TWO_POINTS, CardPoint.getCardPoints(two));
    }

    @Test
    void testAceWorth15() {
        final Card ace = new CardImpl(Seed.HEARTS, CardValue.ACE);
        assertEquals(ACE_POINTS, CardPoint.getCardPoints(ace));
    }

    @Test
    void testHighCardsWorth10() {
        final CardValue[] highValues = {
            CardValue.KING, CardValue.QUEEN, CardValue.JACK, 
            CardValue.TEN, CardValue.NINE, CardValue.EIGHT
        };
        for (final CardValue value : highValues) {
            final Card card = new CardImpl(Seed.HEARTS, value);
            assertEquals(HIGH_CARD_POINTS, CardPoint.getCardPoints(card), "Expected 10 points for value: " + value);
        }
    }

    @Test
    void testLowCardsWorth5() {
        final CardValue[] lowValues = {
            CardValue.SEVEN, CardValue.SIX, CardValue.FIVE, CardValue.FOUR, CardValue.THREE
        };
        for (final CardValue value : lowValues) {
            final Card card = new CardImpl(Seed.HEARTS, value);
            assertEquals(LOW_CARD_POINTS, CardPoint.getCardPoints(card), "Expected 5 points for value: " + value);
        }
    }

    @Test
    void testToIntMapping() {
        final CardValue[] values = {
            CardValue.ACE, CardValue.TWO, CardValue.THREE, CardValue.FOUR, CardValue.FIVE, 
            CardValue.SIX, CardValue.SEVEN, CardValue.EIGHT, CardValue.NINE, CardValue.TEN, 
            CardValue.JACK, CardValue.QUEEN, CardValue.KING
        };
        final int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        for (int i = 0; i < values.length; i++) {
            final Card card = new CardImpl(Seed.HEARTS, values[i]);
            assertEquals(expected[i], CardPoint.toInt(card),
                "Rank-to-int mapping failed for value: " + values[i]);
        }
    }

    @Test
    void testToIntThrowsOnJolly() {
        final Card jolly = new CardImpl(Seed.JOKER, CardValue.JOLLY);
        assertThrows(IllegalArgumentException.class, () -> CardPoint.toInt(jolly));
    }

    @Test
    void testPrivateConstructor() {
        final var constructors = CardPoint.class.getDeclaredConstructors();
        assertEquals(CONSTRUCTOR_COUNT, constructors.length);
        assertFalse(constructors[0].canAccess(null));
    }
}
