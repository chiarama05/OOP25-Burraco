package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;

class SetHandlerTest {

    private final SetHandler handler = new SetHandler();

    @Test
    void testValidSet() {
        final List<Card> set = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FIVE), 
            new CardImpl(Seed.SPADES, CardValue.FIVE), 
            new CardImpl(Seed.DIAMONDS, CardValue.FIVE)
        );
        assertTrue(handler.isValid(set));
    }

    @Test
    void testInvalidSetDifferentValues() {
        final List<Card> set = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FIVE), 
            new CardImpl(Seed.SPADES, CardValue.SIX), 
            new CardImpl(Seed.DIAMONDS, CardValue.FIVE)
        );
        assertFalse(handler.isValid(set));
    }
    
    @Test
    void testSetWithTwoWildcardsIsInvalid() {
        final List<Card> set = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FIVE), 
            new CardImpl(Seed.JOKER, CardValue.JOLLY), 
            new CardImpl(Seed.JOKER, CardValue.JOLLY)
        );
        assertFalse(handler.isValid(set));
    }
}
