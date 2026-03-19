package it.unibo.burraco.core.combination;

import java.util.List;

import it.unibo.burraco.model.card.Card;
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

    if (SetUtils.isValidSet(combination)) {
        return SetAttachUtils.canAttachToSet(combination, newCard);
    }

    if (StraightUtils.isSameSeed(combination)) {
        return StraightAttachUtils.canAttachToStraight(combination, newCard);
    }

    return false;
}
}