package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class StraightUtilsTest {
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String JOLLY_SEED = "♕";
    private static final String JOLLY_VAL = "Jolly";
    private static final String VAL_4 = "4";
    private static final String VAL_5 = "5";
    private static final String VAL_6 = "6";

    private final StraightUtils utils = new StraightUtils();

    @Test
    void testSimpleStraight() {
        final List<Card> straight = List.of(
            new CardImpl(HEARTS, VAL_4), 
            new CardImpl(HEARTS, VAL_5), 
            new CardImpl(HEARTS, VAL_6)
        );
        assertTrue(utils.isValidStraight(straight));
    }

    @Test
    void testStraightWithJolly() {
        final List<Card> straight = List.of(
            new CardImpl(HEARTS, VAL_4), 
            new CardImpl(JOLLY_SEED, JOLLY_VAL), 
            new CardImpl(HEARTS, VAL_6)
        );
        assertTrue(utils.isValidStraight(straight));
    }
    
    @Test
    void testDifferentSeedsInvalid() {
        final List<Card> straight = List.of(
            new CardImpl(HEARTS, VAL_4), 
            new CardImpl(SPADES, VAL_5), 
            new CardImpl(HEARTS, VAL_6)
        );
        assertFalse(utils.isSameSeed(straight));
    }
}
