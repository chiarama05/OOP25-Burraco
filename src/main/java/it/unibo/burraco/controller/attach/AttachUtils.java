package it.unibo.burraco.controller.attach;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.controller.combination.*;
import it.unibo.burraco.controller.combination.set.SetUtils;
import it.unibo.burraco.controller.combination.straight.StraightUtils;

/**
 * Utility class responsible for determining whether one or more cards 
 * can be attached to an existing combination on the table.
 */
public class AttachUtils {

    /**
     * Checks if a list of new cards can be legally added to an existing combination.
     * @param combination the current combination of cards on the table
     * @param newCards the list of cards to attempt to attach
     * @return true if the resulting combination is valid, false otherwise
     */
    public static boolean canAttach(List<Card> combination, List<Card> newCards) {
        if (combination == null || combination.isEmpty()) return false;

        // Filters out wildcards to check for duplicate values in sets
        List<Card> realCards = combination.stream()
            .filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2"))
            .collect(java.util.stream.Collectors.toList());

        boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(java.util.stream.Collectors.toSet()).size() < realCards.size();

        List<Card> hypothetical = new ArrayList<>(combination);
        hypothetical.addAll(newCards);

        // The combination is a Set
        if (hasDuplicateValues || SetUtils.isValidSet(combination)) {
            return CombinationValidator.isValidCombination(hypothetical);
        }

        // The combination is a Straight
        if (StraightUtils.isSameSeed(combination)) {
            return CombinationValidator.isValidCombination(hypothetical);
        }

        return false;
    }

    /**
     * Overloaded method to check if a single card can be attached to a combination.
     * @param combination the existing combination
     * @param newCard the single card to attach
     * @return true if the card can be attached, false otherwise
     */
    public static boolean canAttach(List<Card> combination, Card newCard) {
        return canAttach(combination, List.of(newCard));
    }
}