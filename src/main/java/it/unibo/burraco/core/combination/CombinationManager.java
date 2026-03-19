package it.unibo.burraco.core.combination;

import it.unibo.burraco.model.card.Card;
import java.util.ArrayList;
import java.util.List;

public class CombinationManager {

    private CombinationManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<Card> prepareForDisplay(List<Card> cards) {
        List<Card> orderedCards = new ArrayList<>(cards);
        
        // Logica jolly estratta dalla view
        int jokerIndex = -1;
        for (int i = 0; i < orderedCards.size(); i++) {
            if (orderedCards.get(i).getValue().equals("Jolly")) {
                jokerIndex = i;
                break;
            }
        }

        if (jokerIndex != -1) {
            boolean jokerAtExtremes = (jokerIndex == 0 || jokerIndex == orderedCards.size() - 1);
            if (jokerAtExtremes) {
                Card joker = orderedCards.remove(jokerIndex);
                orderedCards.add(joker);
            }
        }
        return orderedCards;
    }
}
