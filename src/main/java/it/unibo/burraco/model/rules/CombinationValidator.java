package it.unibo.burraco.model.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

/**
 * Utility class responsible for validating whether a set of cards
 * forms a legal game combination (Straight or Set) according to the rules.
 */
public final class CombinationValidator {

    private static final String JOLLY_LITERAL = "Jolly";
    private static final String TWO_LITERAL = "2";
    private static final int MIN_COMBO_SIZE = 3;
    private final SetHandler setHandler;
    private final StraightUtils straightUtils;

    /**
     * Constructs a new CombinationValidator and initializes its internal handlers.
     */
    public CombinationValidator() {
        this.setHandler = new SetHandler();
        this.straightUtils = new StraightUtils();
    }

    /**
     * Validates a combination of cards.
     * Checks for minimum size, type of combination (Straight/Set),
     * and the correct usage of wildcards (Jolly and 2).
     *
     * @param cards the list of cards to validate
     * @return true if the combination is valid, false otherwise
     */
    public boolean isValidCombination(final List<Card> cards) {
        if (cards == null || cards.size() < MIN_COMBO_SIZE) {
            return false;
        }

        final List<Card> realCards = cards.stream()
            .filter(c -> !JOLLY_LITERAL.equalsIgnoreCase(c.getValue()) && !TWO_LITERAL.equals(c.getValue()))
            .collect(Collectors.toList());

        final boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(Collectors.toSet()).size() < realCards.size();

        if (hasDuplicateValues && this.setHandler.isValid(cards)) {
            final long wildcardsInSet = cards.stream()
                .filter(c -> JOLLY_LITERAL.equalsIgnoreCase(c.getValue()) || TWO_LITERAL.equals(c.getValue()))
                .count();
            return wildcardsInSet <= 1;
        }
        if (this.straightUtils.isSameSeed(cards)) {
            final List<Card> ordered = this.straightUtils.orderStraight(new ArrayList<>(cards));
            int effectiveWildcards = 0;
            for (int i = 0; i < ordered.size(); i++) {
                final Card c = ordered.get(i);
                if (JOLLY_LITERAL.equalsIgnoreCase(c.getValue())) {
                    effectiveWildcards++;
                } else if (TWO_LITERAL.equals(c.getValue())
                        && !this.straightUtils.isPositionallyNatural(i, ordered)) {
                    effectiveWildcards++;
                }
            }
            return effectiveWildcards <= 1 && this.straightUtils.isValidStraight(cards);
        }
        if (this.setHandler.isValid(cards)) {
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
    public boolean isWildcard(final Card c, final List<Card> context) {

        if (JOLLY_LITERAL.equalsIgnoreCase(c.getValue())) {
            return true;
        }
        if (!TWO_LITERAL.equals(c.getValue())) {
            return false;
        }
        if (c instanceof CardImpl && ((CardImpl) c).isUsedAsWildcard()) {
            return true;
        }

        final List<Card> realCards = context.stream()
            .filter(r -> !JOLLY_LITERAL.equalsIgnoreCase(r.getValue()) && !TWO_LITERAL.equals(r.getValue()))
            .collect(Collectors.toList());

        final boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(Collectors.toSet()).size() < realCards.size();

        if (hasDuplicateValues || !this.straightUtils.isSameSeed(context)) {
            return true;
        }

        final List<Card> ordered = this.straightUtils.orderStraight(new ArrayList<>(context));
        final int index = ordered.indexOf(c);
        return !this.straightUtils.isPositionallyNatural(index, ordered);
    }
}
