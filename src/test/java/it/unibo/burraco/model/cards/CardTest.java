package it.unibo.burraco.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void testConstructorAndGetters() {
        final CardImpl card = new CardImpl(Seed.HEARTS, CardValue.ACE);
        assertEquals(Seed.HEARTS, card.getSeed());
        assertEquals(CardValue.ACE, card.getValue());
    }

    @Test
    void testWildcardStatus() {
        final CardImpl card = new CardImpl(Seed.HEARTS, CardValue.TWO);
        assertFalse(card.isUsedAsWildcard(), "Default status should be false");
        
        card.setAsWildcard(true);
        assertTrue(card.isUsedAsWildcard(), "Status should be true after set");
    }

    @Test
    void testToString() {
        final CardImpl card = new CardImpl(Seed.SPADES, CardValue.ACE);
        assertEquals("A♠", card.toString());
        
        final CardImpl jolly = new CardImpl(Seed.JOKER, CardValue.JOLLY);
        assertEquals("Jolly♕", jolly.toString());
    }

    @Test
    void testGetNumericalValue() {
        assertEquals(1, new CardImpl(Seed.HEARTS, CardValue.ACE).getNumericalValue());
        assertEquals(2, new CardImpl(Seed.HEARTS, CardValue.TWO).getNumericalValue());
        assertEquals(13, new CardImpl(Seed.HEARTS, CardValue.KING).getNumericalValue());
        assertEquals(0, new CardImpl(Seed.JOKER, CardValue.JOLLY).getNumericalValue());
    }
}
