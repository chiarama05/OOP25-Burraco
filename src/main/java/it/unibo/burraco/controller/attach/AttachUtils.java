package it.unibo.burraco.controller.attach;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.combination.set.SetUtils;
import it.unibo.burraco.controller.combination.straight.StraightUtils;
import it.unibo.burraco.model.card.Card;

/**
 * Utility class responsible for determining whether one or more cards 
 * can be attached to an existing combination on the table.
 */
public class AttachUtils {

    /**
     * Checks if a list of new cards can be legally added to an existing combination.
     * 
     * @param combination the current combination of cards on the table
     * @param newCards the list of cards to attempt to attach
     * @return true if the resulting combination is valid, false otherwise
     */
    public static boolean canAttach(final List<Card> combination, final List<Card> newCards) {
        if (combination == null || combination.isEmpty()) return false;

        final List<Card> realCards = combination.stream()
            .filter(c -> !"Jolly".equalsIgnoreCase(c.getValue()) && !"2".equals(c.getValue()))
            .collect(Collectors.toList());

        final boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(Collectors.toSet()).size() < realCards.size();

        final List<Card> hypothetical = new ArrayList<>(combination);
        hypothetical.addAll(newCards);

        if (hasDuplicateValues || SetUtils.isValidSet(combination)) {
            return CombinationValidator.isValidCombination(hypothetical);
        }

        if (StraightUtils.isSameSeed(combination)) {
            return CombinationValidator.isValidCombination(hypothetical);
        }

        return false;
    }

    /**
     * Overloaded method to check if a single card can be attached to a combination.
     * 
     * @param combination the existing combination
     * @param newCard the single card to attach
     * @return true if the card can be attached, false otherwise
     */
    public static boolean canAttach(final List<Card> combination, final Card newCard) {
        return canAttach(combination, List.of(newCard));
    }
}
