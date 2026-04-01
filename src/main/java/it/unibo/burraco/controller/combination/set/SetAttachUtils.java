package it.unibo.burraco.controller.combination.set;

import java.util.List;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.model.card.Card;

/**
 * Utility class that handles the attachment logic specifically for Set combinations.
 * Provides validation to ensure that any card added to a Set maintains its integrity.
 */
public final class SetAttachUtils {

    private SetAttachUtils() { }

    /**
     * Determines whether a new card can be legally attached to an existing Set.
     * This method enforces the rule that a Set can contain a maximum of one wildcard
     * and that all natural cards must share the same face value.
     *
     * @param set the existing set of cards
     * @param newCard the card to attach
     * @return true if the card can be legally attached to the set, false otherwise
     */
    public static boolean canAttachToSet(final List<Card> set, final Card newCard) {

        // Count how many wildcards are already present in the set
        final long wildcards = set.stream().filter(c -> CombinationValidator.isWildcard(c, set)).count();

        if (CombinationValidator.isWildcard(newCard, set)) {
            return wildcards < 1;
        }

        // Identify the value of the Set by finding the first non-wildcard card
        final String baseValue = set.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, set))
                .map(Card::getValue)
                .findFirst()
                .orElse(newCard.getValue());

        return newCard.getValue().equals(baseValue);
    }
}
