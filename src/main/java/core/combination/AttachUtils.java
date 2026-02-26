package core.combination;

import java.util.List;

import model.card.Card;
/**
 * Utility class responsible for determining whether a card can be attached to an existing combination.
 *
 * @param combination the existing combination of cards
 * @param newCard the card to attempt to attach
 * @return true if the card can be attached according to game rules, false otherwise
 */
public class AttachUtils {

    /**
     * Determines whether a new card can be legally attached to the given combination
     */
    public static boolean canAttach(List<Card> combination, Card newCard) {

        // A card cannot be attached to a null or empty combination
        if (combination == null || combination.isEmpty()) {
            return false;
        }

        if (StraightUtils.isStraight(combination)) {
            return StraightAttachUtils.canAttachToStraight(combination, newCard);
        }

        return SetAttachUtils.canAttachToSet(combination, newCard);
    }
}