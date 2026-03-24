package it.unibo.burraco.controller.combination;

import it.unibo.burraco.model.card.Card;
import java.util.List;
import java.util.stream.Collectors;

public class SetUtils {

    public static boolean isValidSet(List<Card> cards) {
    List<Card> naturalCards = cards.stream()
            .filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2"))
            .collect(Collectors.toList());

    if (naturalCards.isEmpty()) {
        return false; 
    }

    String baseValue = naturalCards.get(0).getValue();

    return naturalCards.stream().allMatch(c -> c.getValue().equals(baseValue));
    }


    public static boolean canAttachCard(List<Card> set, Card newCard) {

        long wildcards = set.stream().filter(c -> CombinationValidator.isWildcard(c, set)).count();

        if (CombinationValidator.isWildcard(newCard, set)){
            return wildcards < 1;
        }
        String baseValue = set.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, set))
                .map(Card::getValue)
                .findFirst()
                .orElse(newCard.getValue());

        return newCard.getValue().equals(baseValue);
    }

    public static boolean isNaturalTwoInSet(Card two, List<Card> cards) {
    if (!two.getValue().equals("2")) return false;

    // In un Tris, il 2 è naturale se ha lo stesso seme delle altre carte del tris
    // (che per definizione devono avere tutte lo stesso valore, es. tutti 7)
    return cards.stream()
            .filter(c -> !c.getValue().equals("2") && !c.getValue().equalsIgnoreCase("Jolly"))
            .findFirst()
            .map(firstReal -> firstReal.getSeed().equals(two.getSeed()))
            .orElse(false);
}
}