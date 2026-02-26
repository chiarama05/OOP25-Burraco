package core.combination;

import java.util.List;
import card.Card;

public class AttachUtils {

    public static boolean canAttach(List<Card> combination, Card newCard) {

        if (combination == null || combination.isEmpty()) {
            return false;
        }

        if (StraightUtils.isStraight(combination)) {
            return StraightAttachUtils.canAttachToStraight(combination, newCard);
        }

        return SetAttachUtils.canAttachToSet(combination, newCard);
    }
}