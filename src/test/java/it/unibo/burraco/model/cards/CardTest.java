package it.unibo.burraco.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.score.CardPoint;

class CardTest {

    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String JOLLY_SEED = "♕";

    @Test
    void testConstructorAndGetters() {
        final CardImpl card = new CardImpl(HEARTS, "A");
        assertEquals(HEARTS, card.getSeed());
        assertEquals("A", card.getValue());
    }

    @Test
    void testWildcardStatus() {
        final CardImpl card = new CardImpl(HEARTS, "2");
        assertFalse(card.isUsedAsWildcard(), "Default status should be false");
        
        card.setAsWildcard(true);
        assertTrue(card.isUsedAsWildcard(), "Status should be true after set");
    }

    @Test
    void testToString() {
        final CardImpl card = new CardImpl(SPADES, "A");
        assertEquals("A♠", card.toString());
    }

    @Test
    void testGetNumericalValue() {
        assertEquals(CardPoint.RANK_ACE, new CardImpl(HEARTS, "A").getNumericalValue());
        assertEquals(CardPoint.RANK_TWO, new CardImpl(HEARTS, "2").getNumericalValue());
        assertEquals(CardPoint.RANK_KING, new CardImpl(HEARTS, "K").getNumericalValue());
        assertEquals(0, new CardImpl(JOLLY_SEED, "Jolly").getNumericalValue());
        assertEquals(-1, new CardImpl(HEARTS, "Invalid").getNumericalValue());
    }
}