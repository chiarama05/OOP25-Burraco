package it.unibo.burraco.controller.combination.set;

import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.model.card.Card;

/**
 * Utility class providing helper methods for Set combinations.
 * A Set is considered valid if all non-wildcard cards share the same face value.
 */
public final class SetUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SetUtils() { }

    /**
     * Validates whether a list of cards forms a legal Set.
     * The method filters out wildcards and ensures all remaining cards have an identical face value.
     *
     * @param cards the list of cards to be validated
     * @return true if all natural cards in the list share the same value, false if empty or mismatched
     */
    public static boolean isValidSet(final List<Card> cards) {
        final List<Card> naturalCards = cards.stream()
                .filter(c -> !"Jolly".equalsIgnoreCase(c.getValue()) && !"2".equals(c.getValue()))
                .collect(Collectors.toList());

        if (naturalCards.isEmpty()) {
            return false;
        }

        final String baseValue = naturalCards.get(0).getValue();
        return naturalCards.stream().allMatch(c -> c.getValue().equals(baseValue));
    }

    /**
     * Determines if a specific card can be added to an existing Set combination.
     * It checks the current wildcard count and verifies that the new card's value
     * matches the established value of the set.
     *
     * @param set     the existing set of cards on the table
     * @param newCard the card to attempt to attach
     * @return true if the card matches the set's value or is a valid wildcard addition
     */
    public static boolean canAttachCard(final List<Card> set, final Card newCard) {
        final long wildcards = set.stream()
                .filter(c -> CombinationValidator.isWildcard(c, set))
                .count();

        if (CombinationValidator.isWildcard(newCard, set)) {
            return wildcards < 1;
        }

        final String baseValue = set.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, set))
                .map(Card::getValue)
                .findFirst()
                .orElse(newCard.getValue());

        return newCard.getValue().equals(baseValue);
    }
}
