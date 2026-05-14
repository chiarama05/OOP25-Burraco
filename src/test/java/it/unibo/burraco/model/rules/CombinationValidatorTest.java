package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class CombinationValidatorTest {
    private static final String HEART_SYMBOL = "♥";
    private static final String VAL_5 = "5";
    private static final String VAL_7 = "7";
    private final CombinationValidator validator = new CombinationValidator();

    @Test
    void testCombinationTooShort() {
        final List<Card> shortCombo = List.of(new CardImpl(HEART_SYMBOL, VAL_5), new CardImpl(HEART_SYMBOL, "6"));
        assertFalse(validator.isValidCombination(shortCombo));
    }

    @Test
    void testValidSetCombination() {
        final List<Card> set = List.of(
            new CardImpl(HEART_SYMBOL, VAL_7), new CardImpl("♠", VAL_7), new CardImpl("♦", VAL_7));
        assertTrue(validator.isValidCombination(set));
    }
}
