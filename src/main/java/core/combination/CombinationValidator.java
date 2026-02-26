package core.combination;

import java.util.*;
import model.card.*;

public class CombinationValidator {

    public static boolean isValidCombination(List<Card> cards) {
        if (cards == null || cards.size() < 3) {
            return false;
        }
    
        long wildcards = cards.stream().filter(c -> isWildcard(c, cards)).count();
        if (wildcards > 1){
            return false;
        } 
        if (StraightUtils.isStraight(cards)) {
            return StraightUtils.isValidStraight(cards);
        }
        return SetUtils.isValidSet(cards);
    }

    public static boolean isWildcard(Card c, List<Card> context) {
        if (c.getValue().equals("Jolly")) return true;
        if (!c.getValue().equals("2")) return false;
        return !StraightUtils.isNaturalTwo(c, context);
    }
}