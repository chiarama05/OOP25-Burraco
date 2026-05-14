package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class SetHandlerTest {
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String DIAMONDS = "♦";
    private static final String JOLLY_SEED = "♕";
    private static final String JOLLY_VAL = "Jolly";
    private static final String VAL_5 = "5";
    private static final String VAL_6 = "6";

    private final SetHandler handler = new SetHandler();

    @Test
    void testValidSet() {
        final List<Card> set = List.of(
            new CardImpl(HEARTS, VAL_5), 
            new CardImpl(SPADES, VAL_5), 
            new CardImpl(DIAMONDS, VAL_5)
        );
        assertTrue(handler.isValid(set));
    }

    @Test
    void testInvalidSetDifferentValues() {
        final List<Card> set = List.of(
            new CardImpl(HEARTS, VAL_5), 
            new CardImpl(SPADES, VAL_6), 
            new CardImpl(DIAMONDS, VAL_5)
        );
        assertFalse(handler.isValid(set));
    }
    
    @Test
    void testSetWithTwoWildcardsIsInvalid() {
        final List<Card> set = List.of(
            new CardImpl(HEARTS, VAL_5), 
            new CardImpl(JOLLY_SEED, JOLLY_VAL), 
            new CardImpl(JOLLY_SEED, JOLLY_VAL)
        );
        assertFalse(handler.isValid(set));
    }
}
