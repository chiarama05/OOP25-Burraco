package it.unibo.burraco.controller.combination;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.controller.combination.set.SetUtils;
import it.unibo.burraco.controller.combination.straight.StraightUtils;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

/**
 * Utility class responsible for validating whether a set of cards
 * forms a legal game combination (Straight or Set) according to the rules.
 */
public final class CombinationValidator {

    private static final String JOLLY_LITERAL = "Jolly";
    private static final String TWO_LITERAL = "2";
    private static final int MIN_COMBO_SIZE = 3;

    private CombinationValidator() { }

    /**
     * Validates a combination of cards.
     * Checks for minimum size, type of combination (Straight/Set),
     * and the correct usage of wildcards (Jolly and 2).
     *
     * @param cards the list of cards to validate
     * @return true if the combination is valid, false otherwise
     */
    public static boolean isValidCombination(final List<Card> cards) {
        // A combination must have at least 3 cards
        if (cards == null || cards.size() < MIN_COMBO_SIZE) {
            return false;
        }

        // Filter cards that are not wildcards
        final List<Card> realCards = cards.stream()
            .filter(c -> !JOLLY_LITERAL.equalsIgnoreCase(c.getValue()) && !TWO_LITERAL.equals(c.getValue()))
            .collect(Collectors.toList());

        // Check if there are duplicate values
        final boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(Collectors.toSet()).size() < realCards.size();

        // Case 1: The combination is a Set
        if (hasDuplicateValues && SetUtils.isValidSet(cards)) {
            final long wildcardsInSet = cards.stream()
                .filter(c -> JOLLY_LITERAL.equalsIgnoreCase(c.getValue()) || TWO_LITERAL.equals(c.getValue()))
                .count();
            return wildcardsInSet <= 1;
        }

        // Case 2: The combination is a Straight
        if (StraightUtils.isSameSeed(cards)) {
            final List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            int effectiveWildcards = 0;
            for (int i = 0; i < ordered.size(); i++) {
                final Card c = ordered.get(i);
                if (JOLLY_LITERAL.equalsIgnoreCase(c.getValue())) {
                    effectiveWildcards++;
                } else if (TWO_LITERAL.equals(c.getValue())
                        && !StraightUtils.isPositionallyNatural(i, ordered)) {
                    effectiveWildcards++;
                }
            }
            return effectiveWildcards <= 1 && StraightUtils.isValidStraight(cards);
        }

        // Final check for Sets without duplicate cards
        if (SetUtils.isValidSet(cards)) {
            final long wildcardsInSet = cards.stream()
                .filter(c -> JOLLY_LITERAL.equalsIgnoreCase(c.getValue()) || TWO_LITERAL.equals(c.getValue()))
                .count();
            return wildcardsInSet <= 1;
        }
        return false;
    }

    /**
     * Determines if a specific card is acting as a wildcard.
     *
     * @param c the card to check
     * @param context the combination of cards the card belongs to
     * @return true if the card is a Joker or a "2" used as a wildcard, false otherwise
     */
    public static boolean isWildcard(final Card c, final List<Card> context) {

        if (JOLLY_LITERAL.equalsIgnoreCase(c.getValue())) {
            return true;
        }
        if (!TWO_LITERAL.equals(c.getValue())) {
            return false;
        }
        if (c instanceof CardImpl && ((CardImpl) c).isUsedAsWildcard()) {
            return true;
        }

        // Analysis for the "2"
        final List<Card> realCards = context.stream()
            .filter(r -> !JOLLY_LITERAL.equalsIgnoreCase(r.getValue()) && !TWO_LITERAL.equals(r.getValue()))
            .collect(Collectors.toList());

        final boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(Collectors.toSet()).size() < realCards.size();

        // In a Set a "2" is always a wildcard
        if (hasDuplicateValues || !StraightUtils.isSameSeed(context)) {
            return true;
        }

        // In a Straight a "2" is a wildcard only if not in its natural sequence
        final List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(context));
        final int index = ordered.indexOf(c);
        return !StraightUtils.isPositionallyNatural(index, ordered);
    }
}
