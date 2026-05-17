package it.unibo.burraco.controller.display;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.rules.StraightUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sorts combinations into display order before they are passed to the view.
 * The view always receives cards already ordered, with no knowledge of sorting logic.
 */
public final class CombinationDisplaySorter {

    private final StraightUtils straightUtils = new StraightUtils();

    /**
     * Sorts a combination of cards into the correct display order.
     * If the combination is a valid straight, cards are ordered by rank descending.
     * Otherwise the combination is treated as a set and sorted via sortAsSet.
     *
     * @param cards the combination to sort; must not be null
     * @return a new list with cards in display order
     */
    public List<Card> sortForDisplay(final List<Card> cards) {
        if (straightUtils.isSameSeed(cards)
                && straightUtils.isValidStraight(new ArrayList<>(cards))) {
            final List<Card> ordered = straightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered);
            return ordered;
        }
        return sortAsSet(cards);
    }

    /**
     * Sorts a set-type combination for display.
     * Wildcards (Jolly and any Two not acting as a natural card) are placed first,
     * followed by natural cards sorted by numerical value in descending order.
     *
     * @param cards the set combination to sort
     * @return a new list with wildcards first, then naturals in descending rank order
     */
    private List<Card> sortAsSet(final List<Card> cards) {
        final CardValue baseValue = cards.stream()
                .filter(c -> !c.getValue().isPotentialWildcard())
                .map(Card::getValue)
                .findFirst()
                .orElse(null);

        final List<Card> wildcards = new ArrayList<>();
        final List<Card> naturals = new ArrayList<>();

        for (final Card c : cards) {
            final boolean isWild = c.getValue().isJolly()
                    || c.getValue() == CardValue.TWO && c.getValue() != baseValue;
            if (isWild) {
                wildcards.add(c);
            } else {
                naturals.add(c);
            }
        }

        naturals.sort((a, b) ->
                Integer.compare(b.getNumericalValue(), a.getNumericalValue()));

        final List<Card> result = new ArrayList<>(wildcards);
        result.addAll(naturals);
        return result;
    }
}
