package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.CardImpl;

class CardTest {

    private static final String HEARTS = "♥";
    private static final String ACE = "A";
    private static final String KING = "K";
    private static final String TWO = "2";
    private static final String JOLLY = "Jolly";
    private static final String INVALID = "InvalidValue";
    
    private static final int ACE_NUM = 1;
    private static final int KING_NUM = 13;
    private static final int TWO_NUM = 2;
    private static final int JOLLY_NUM = 0;
    private static final int ERROR_NUM = -1;

    private CardImpl aceOfHearts;

    @BeforeEach
    void init() {
        this.aceOfHearts = new CardImpl(HEARTS, ACE);
    }

    @Test
    void testInitialState() {
        assertEquals(HEARTS, aceOfHearts.getSeed());
        assertEquals(ACE, aceOfHearts.getValue());
        assertEquals(ACE_NUM, aceOfHearts.getNumericalValue());
        assertFalse(aceOfHearts.isUsedAsWildcard());
    }

    @Test
    void testNumericalValueMapping() {
        final CardImpl king = new CardImpl(HEARTS, KING);
        final CardImpl two = new CardImpl(HEARTS, TWO);
        final CardImpl jolly = new CardImpl(JOLLY, JOLLY);
        final CardImpl invalid = new CardImpl(HEARTS, INVALID);

        assertEquals(KING_NUM, king.getNumericalValue());
        assertEquals(TWO_NUM, two.getNumericalValue());
        assertEquals(JOLLY_NUM, jolly.getNumericalValue());
        assertEquals(ERROR_NUM, invalid.getNumericalValue());
    }

    @Test
    void testWildcardToggle() {
        aceOfHearts.setAsWildcard(true);
        assertTrue(aceOfHearts.isUsedAsWildcard());
        
        aceOfHearts.setAsWildcard(false);
        assertFalse(aceOfHearts.isUsedAsWildcard());
    }

    @Test
    void testToStringFormat() {

        final String expected = ACE + HEARTS;
        assertEquals(expected, aceOfHearts.toString());
    }

    @Test
    void testEqualitySameState() {
        final CardImpl anotherAce = new CardImpl(HEARTS, ACE);

        assertTrue(aceOfHearts != anotherAce);
    }

}
