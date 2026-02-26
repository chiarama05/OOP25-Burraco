package core.combination;

import java.util.List;

import model.card.Card;

public class SetAttachUtils {

    public static boolean canAttachToSet(List<Card> set, Card newCard) {

        long wildcards = set.stream()
                .filter(c -> CombinationValidator.isWildcard(c, set))
                .count();

        // Se la nuova è matta
        if (CombinationValidator.isWildcard(newCard, set)) {
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
