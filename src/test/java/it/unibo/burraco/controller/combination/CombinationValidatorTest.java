package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class CombinationValidatorTest {
    @Test
    void testInvalidShortCombination() {
        List<Card> cards = List.of(new CardImpl("♥", "7"), new CardImpl("♠", "7"));
        assertFalse(CombinationValidator.isValidCombination(cards)); 
    }

    @Test
    void testValidSetWithOneWildcard() {
        List<Card> cards = List.of(
            new CardImpl("♥", "7"), 
            new CardImpl("♠", "7"), 
            new CardImpl("♦", "2") 
        );
        assertTrue(CombinationValidator.isValidCombination(cards));
    }

    @Test
    void testInvalidSetWithTwoWildcards() {
        List<Card> cards = List.of(
            new CardImpl("♥", "7"), 
            new CardImpl("Jolly", "Jolly"), 
            new CardImpl("♦", "2") 
        );
        assertFalse(CombinationValidator.isValidCombination(cards));
    }

    @Test
    void testValidStraightWithNaturalTwo() {
        List<Card> cards = List.of(
            new CardImpl("♥", "A"), 
            new CardImpl("♥", "2"), 
            new CardImpl("♥", "3")
        );
        assertTrue(CombinationValidator.isValidCombination(cards));
    }
}
