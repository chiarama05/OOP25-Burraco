package it.unibo.burraco.controller.combination;

import it.unibo.burraco.model.card.Card;
import java.util.ArrayList;
import java.util.List;

public class CombinationManager {

    private CombinationManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<Card> prepareForDisplay(List<Card> cards) {
    List<Card> orderedCards = new ArrayList<>(cards);

    List<Card> wildcards = new ArrayList<>();
    List<Card> naturals = new ArrayList<>();

    for (Card c : orderedCards) {
        if (c.getValue().equalsIgnoreCase("Jolly") || CombinationValidator.isWildcard(c, orderedCards)) {
            wildcards.add(c);
        } else {
            naturals.add(c);
        }
    }

    orderedCards.clear();
    orderedCards.addAll(naturals);
    orderedCards.addAll(wildcards);   

    return orderedCards;
}
}
