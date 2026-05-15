package it.unibo.burraco.controller;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.rules.StraightUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sorts combinations into display order before they enter the GameState DTO.
 * Lives in the controller — the only layer allowed to use model.rules.
 * The view receives cards already ordered and does zero domain logic.
 */
public final class CombinationDisplaySorter {

    private static final String JOLLY = "Jolly";
    private static final String TWO   = "2";

    private final StraightUtils straightUtils = new StraightUtils();

    public List<Card> sortForDisplay(final List<Card> cards) {
        if (straightUtils.isSameSeed(cards)
                && straightUtils.isValidStraight(new ArrayList<>(cards))) {
            final List<Card> ordered = straightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered);
            return ordered;
        }
        return sortAsSet(cards);
    }

    private List<Card> sortAsSet(final List<Card> cards) {
        final String baseValue = cards.stream()
            .filter(c -> !JOLLY.equalsIgnoreCase(c.getValue())
                      && !TWO.equals(c.getValue()))
            .map(Card::getValue)
            .findFirst()
            .orElse(null);

        final List<Card> wildcards = new ArrayList<>();
        final List<Card> naturals  = new ArrayList<>();

        for (final Card c : cards) {
            final boolean isWild = JOLLY.equalsIgnoreCase(c.getValue())
                || (TWO.equals(c.getValue()) && !TWO.equals(baseValue));
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