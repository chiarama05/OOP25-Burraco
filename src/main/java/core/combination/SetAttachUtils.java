package core.combination;

import java.util.List;

import model.card.Card;

/**
 * Utility class that handles the attachment logic for Set combinations.
 * A Set is a combination of cards that all share the same value
 */
public class SetAttachUtils {

    /**
     * Determines whether a new card can be legally attached to an existing Set.
     *
     * @param set the existing set of cards
     * @param newCard the card to attach
     * @return true if the card can be legally attached to the set, false otherwise
     */
    public static boolean canAttachToSet(List<Card> set, Card newCard) {

        // Count how many wildcards are already present in the set
        long wildcards = set.stream().filter(c -> CombinationValidator.isWildcard(c, set)).count();

        // If the new card is a wildcard, allow it only if there is not already a wildcard in the set
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
