package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class CombinationValidatorTest {
    private final CombinationValidator validator = new CombinationValidator();

    @Test
    void testCombinationTooShort() {
        final List<Card> shortCombo = List.of(new CardImpl("♥", "5"), new CardImpl("♥", "6"));
        assertFalse(validator.isValidCombination(shortCombo));
    }

    @Test
    void testValidSetCombination() {
        final List<Card> set = List.of(
            new CardImpl("♥", "7"), new CardImpl("♠", "7"), new CardImpl("♦", "7"));
        assertTrue(validator.isValidCombination(set));
    }
}
