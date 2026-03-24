package it.unibo.burraco.controller.attach;

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

    public static boolean canAttach(List<Card> combination, Card newCard) {
    if (combination == null || combination.isEmpty()) return false;

    List<Card> realCards = combination.stream()
        .filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2"))
        .collect(java.util.stream.Collectors.toList());

    boolean hasDuplicateValues = realCards.stream()
        .map(Card::getValue)
        .collect(java.util.stream.Collectors.toSet()).size() < realCards.size();

    if (hasDuplicateValues) {
        return SetAttachUtils.canAttachToSet(combination, newCard);
    }

    if (StraightUtils.isSameSeed(combination)) {
        return StraightAttachUtils.canAttachToStraight(combination, newCard);
    }

    if (SetUtils.isValidSet(combination)) {
        return SetAttachUtils.canAttachToSet(combination, newCard);
    }

    return false;
}
}