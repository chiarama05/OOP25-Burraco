package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class StraightUtilsTest {
    private final StraightUtils utils = new StraightUtils();

    @Test
    void testSimpleStraight() {
        final List<Card> straight = List.of(
            new CardImpl("♥", "4"), new CardImpl("♥", "5"), new CardImpl("♥", "6"));
        assertTrue(utils.isValidStraight(straight));
    }

    @Test
    void testStraightWithJolly() {
        final List<Card> straight = List.of(
            new CardImpl("♥", "4"), new CardImpl("♕", "Jolly"), new CardImpl("♥", "6"));
        assertTrue(utils.isValidStraight(straight));
    }
    
    @Test
    void testDifferentSeedsInvalid() {
        final List<Card> straight = List.of(
            new CardImpl("♥", "4"), new CardImpl("♠", "5"), new CardImpl("♥", "6"));
        assertFalse(utils.isSameSeed(straight));
    }
}
