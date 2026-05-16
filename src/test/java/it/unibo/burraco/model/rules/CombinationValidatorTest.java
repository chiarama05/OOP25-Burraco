package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;

class CombinationValidatorTest {
    private final CombinationValidator validator = new CombinationValidator();

    @Test
    void testCombinationTooShort() {
        final List<Card> shortCombo = List.of(
            new CardImpl(Seed.HEARTS, CardValue.FIVE), 
            new CardImpl(Seed.HEARTS, CardValue.SIX)
        );
        assertFalse(validator.isValidCombination(shortCombo));
    }

    @Test
    void testValidSetCombination() {
        final List<Card> set = List.of(
            new CardImpl(Seed.HEARTS, CardValue.SEVEN), 
            new CardImpl(Seed.SPADES, CardValue.SEVEN), 
            new CardImpl(Seed.DIAMONDS, CardValue.SEVEN)
        );
        assertTrue(validator.isValidCombination(set));
    }
}
