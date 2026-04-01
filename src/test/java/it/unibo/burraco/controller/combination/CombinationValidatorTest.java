package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class CombinationValidatorTest {

    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String DIAMONDS = "♦";
    private static final String SEVEN = "7";
    private static final String TWO = "2";
    private static final String JOLLY = "Jolly";

    @Test
    void testInvalidShortCombination() {
        final List<Card> cards = List.of(new CardImpl(HEARTS, SEVEN), new CardImpl(SPADES, SEVEN));
        assertFalse(CombinationValidator.isValidCombination(cards));
    }

    @Test
    void testValidSetWithOneWildcard() {
        final List<Card> cards = List.of(
            new CardImpl(HEARTS, SEVEN),
            new CardImpl(SPADES, SEVEN),
            new CardImpl(DIAMONDS, TWO)
        );
        assertTrue(CombinationValidator.isValidCombination(cards));
    }

    @Test
    void testInvalidSetWithTwoWildcards() {
        final List<Card> cards = List.of(
            new CardImpl(HEARTS, SEVEN),
            new CardImpl(JOLLY, JOLLY),
            new CardImpl(DIAMONDS, TWO)
        );
        assertFalse(CombinationValidator.isValidCombination(cards));
    }

    @Test
    void testValidStraightWithNaturalTwo() {
        final List<Card> cards = List.of(
            new CardImpl(HEARTS, "A"),
            new CardImpl(HEARTS, TWO),
            new CardImpl(HEARTS, "3")
        );
        assertTrue(CombinationValidator.isValidCombination(cards));
    }
}
