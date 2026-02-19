package combination.SetUtils;
import combination.CombinationValidator.CombinationValidator;

import java.util.List;
import java.util.stream.Collectors;
import card.Card;

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
}
