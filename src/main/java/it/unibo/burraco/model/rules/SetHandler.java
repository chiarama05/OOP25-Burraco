package it.unibo.burraco.model.rules;

import java.util.List;

import it.unibo.burraco.model.cards.Card;

/**
 * Handler class responsible for validating and managing card combinations of type "Set".
 * A Set consists of cards with the same numerical value but different seeds.
 *
 * <p>All raw string comparisons for wildcards have been replaced with
 * {@link it.unibo.burraco.model.cards.CardValue#isPotentialWildcard()}.
 */
public class SetHandler {

    /**
     * Constructs a new SetHandler.
     */
    public SetHandler() {
        // Empty constructor for utility purposes
    }

    /**
     * Validates if a given list of cards forms a legal Set.
     * A Set is valid if all natural cards have the same value and there is
     * at most one wildcard (Jolly or 2).
     *
     * @param cards the list of cards to validate
     * @return true if the cards form a valid Set, false otherwise
     */
    public boolean isValid(final List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }

        final List<Card> naturalCards = cards.stream()
                .filter(c -> !isWildcard(c))
                .toList();

        if (naturalCards.isEmpty()) {
            return false;
        }

        final long wildcardCount = cards.stream().filter(SetHandler::isWildcard).count();
        if (wildcardCount > 1) {
            return false;
        }

        final var baseValue = naturalCards.get(0).getValue();
        return naturalCards.stream().allMatch(c -> c.getValue() == baseValue);
    }

    /**
     * Determines if a specific card can be attached to an existing Set on the table.
     *
     * @param set     the existing combination of cards (the Set)
     * @param newCard the card to be added
     * @return true if the card can be legally added to the Set, false otherwise
     */
    public boolean canAttach(final List<Card> set, final Card newCard) {
        if (set == null || newCard == null) {
            return false;
        }

        final long wildcards = set.stream().filter(SetHandler::isWildcard).count();

        if (isWildcard(newCard)) {
            return wildcards < 1;
        }

        return set.stream()
                .filter(c -> !isWildcard(c))
                .findFirst()
                .map(c -> c.getValue() == newCard.getValue())
                .orElse(true);
    }

    /**
     * In a Set, any Jolly or any Two is always a wildcard.
     *
     * @param c the card to check
     * @return true if the card is a potential wildcard
     */
    private static boolean isWildcard(final Card c) {
        return c.getValue().isPotentialWildcard();
    }
}