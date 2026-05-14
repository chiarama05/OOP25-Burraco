package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class SetHandlerTest {
    private final SetHandler handler = new SetHandler();

    @Test
    void testValidSet() {
        final List<Card> set = List.of(
            new CardImpl("♥", "5"), new CardImpl("♠", "5"), new CardImpl("♦", "5"));
        assertTrue(handler.isValid(set));
    }

    @Test
    void testInvalidSetDifferentValues() {
        final List<Card> set = List.of(
            new CardImpl("♥", "5"), new CardImpl("♠", "6"), new CardImpl("♦", "5"));
        assertFalse(handler.isValid(set));
    }
    
    @Test
    void testSetWithTwoWildcardsIsInvalid() {
        final List<Card> set = List.of(
            new CardImpl("♥", "5"), new CardImpl("♕", "Jolly"), new CardImpl("♕", "Jolly"));
        assertFalse(handler.isValid(set));
    }
}
