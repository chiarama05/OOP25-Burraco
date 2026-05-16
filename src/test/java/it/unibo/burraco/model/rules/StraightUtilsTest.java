package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;

class StraightUtilsTest {

    private final StraightUtils utils = new StraightUtils();

    @Test
    void testSimpleStraight() {
        final List<Card> straight = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FOUR), 
            new CardImpl(Seed.HEARTS, CardValue.FIVE), 
            new CardImpl(Seed.HEARTS, CardValue.SIX)
        );
        assertTrue(utils.isValidStraight(straight));
    }

    @Test
    void testStraightWithJolly() {
        final List<Card> straight = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FOUR), 
            new CardImpl(Seed.JOKER, CardValue.JOLLY), 
            new CardImpl(Seed.HEARTS, CardValue.SIX)
        );
        assertTrue(utils.isValidStraight(straight));
    }
    
    @Test
    void testDifferentSeedsInvalid() {
        final List<Card> straight = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FOUR), 
            new CardImpl(Seed.SPADES, CardValue.FIVE), 
            new CardImpl(Seed.HEARTS, CardValue.SIX)
        );
        assertFalse(utils.isSameSeed(straight));
    }
}
