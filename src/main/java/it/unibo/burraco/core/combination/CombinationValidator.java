package it.unibo.burraco.core.combination;

import it.unibo.burraco.model.card.*;

import java.util.*;

public class CombinationValidator {

    public static boolean isValidCombination(List<Card> cards) {
        if (cards == null || cards.size() < 3) {
            return false;
        }

        if (SetUtils.isValidSet(cards)) {
        long wildcardsInSet = cards.stream()
                .filter(c -> c.getValue().equalsIgnoreCase("Jolly") || c.getValue().equals("2"))
                .count();
        
        return wildcardsInSet <= 1;
        }


        if (StraightUtils.isSameSeed(cards)) {
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
        
        int effectiveWildcards = 0;
        for (int i = 0; i < ordered.size(); i++) {
            Card c = ordered.get(i);
            if (c.getValue().equalsIgnoreCase("Jolly")) {
                effectiveWildcards++;
            } else if (c.getValue().equals("2")) {
                
                if (!StraightUtils.isPositionallyNatural(i, ordered)) {
                    effectiveWildcards++;}
                }
            }
            if (effectiveWildcards > 1) {
             return false;
            }

          
            return StraightUtils.isValidStraight(cards);
        }

        return false;
    }

    public static boolean isWildcard(Card c, List<Card> context) {
        if (c.getValue().equalsIgnoreCase("Jolly")) return true;
        if (!c.getValue().equals("2")) return false;

        if (c instanceof CardImpl && ((CardImpl) c).isUsedAsWildcard()) return true;

        if (StraightUtils.isSameSeed(context)) {
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(context));
        int index = ordered.indexOf(c);
        return !StraightUtils.isPositionallyNatural(index, ordered);
    } 
    
    return true;
    }

}
