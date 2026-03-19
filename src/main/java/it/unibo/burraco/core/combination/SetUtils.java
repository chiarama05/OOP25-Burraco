package it.unibo.burraco.core.combination;

import it.unibo.burraco.model.card.Card;
import java.util.List;
import java.util.stream.Collectors;

public class SetUtils {

    public static boolean isValidSet(List<Card> cards) {

        List<Card> real = cards.stream().filter(c -> !CombinationValidator.isWildcard(c, cards)).collect(Collectors.toList());

        if (real.isEmpty()){
            return false;
        } 
        String value = real.get(0).getValue();
        for (Card c : real) {
            if (!c.getValue().equals(value)){
                return false;
            } 
        }
        return true;
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