package it.unibo.burraco.controller.attach;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.controller.combination.*;;
/**
 * Utility class responsible for determining whether a card can be attached to an existing combination.
 *
 * @param combination the existing combination of cards
 * @param newCard the card to attempt to attach
 * @return true if the card can be attached according to game rules, false otherwise
 */
public class AttachUtils {

    
public static boolean canAttach(List<Card> combination, List<Card> newCards) {
    if (combination == null || combination.isEmpty()) return false;

    List<Card> realCards = combination.stream()
        .filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2"))
        .collect(java.util.stream.Collectors.toList());

    boolean hasDuplicateValues = realCards.stream()
        .map(Card::getValue)
        .collect(java.util.stream.Collectors.toSet()).size() < realCards.size();

    List<Card> hypothetical = new ArrayList<>(combination);
    hypothetical.addAll(newCards);

    if (hasDuplicateValues || SetUtils.isValidSet(combination)) {
        return CombinationValidator.isValidCombination(hypothetical);
    }

    if (StraightUtils.isSameSeed(combination)) {
        return CombinationValidator.isValidCombination(hypothetical);
    }

    return false;
}

public static boolean canAttach(List<Card> combination, Card newCard) {
    return canAttach(combination, List.of(newCard));
}
}