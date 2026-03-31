package it.unibo.burraco.controller.combination;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.model.card.Card;

/**
 * Utility class for managing and formatting card combinations for display purposes.
 */
public final class CombinationManager {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CombinationManager() { }

    /**
     * Prepares a list of cards for visual display.
     * 
     * @param cards the original list of cards in the combination
     * @return a new list of cards ordered for display
     */
    public static List<Card> prepareForDisplay(final List<Card> cards) {
        final List<Card> orderedCards = new ArrayList<>(cards);

        int jokerIndex = -1;
        for (int i = 0; i < orderedCards.size(); i++) {
            if ("Jolly".equals(orderedCards.get(i).getValue())) {
                jokerIndex = i;
                break;
            }
        }

        if (jokerIndex != -1) {
            final boolean jokerAtExtremes = jokerIndex == 0 || jokerIndex == orderedCards.size() - 1;
            if (jokerAtExtremes) {
                final Card joker = orderedCards.remove(jokerIndex);
                orderedCards.add(joker);
            }
        }
        return orderedCards;
    }
}
