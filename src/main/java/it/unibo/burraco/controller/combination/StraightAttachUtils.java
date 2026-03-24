package it.unibo.burraco.controller.combination;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.model.card.Card;

public class StraightAttachUtils {

   public static boolean canAttachToStraight(List<Card> straight, Card newCard) {
       
        List<Card> potentialStraight = new ArrayList<>(straight);
    potentialStraight.add(newCard);

    if (CombinationValidator.isValidCombination(potentialStraight)) {
        return true;
    }

    return canSubstituteInternalWildcard(straight, newCard);
    }

    private static boolean canSubstituteInternalWildcard(List<Card> straight, Card newCard) {
        List<Card> ord = StraightUtils.orderStraight(straight);
        
        for (int i = 0; i < ord.size(); i++) {
            Card current = ord.get(i);
            
            
            if (CombinationValidator.isWildcard(current, straight)) {

                if (i > 0 && i < ord.size() - 1) {
                    Card prev = ord.get(i - 1);
                    Card next = ord.get(i + 1);

                
                    if (!CombinationValidator.isWildcard(prev, straight) && 
                        !CombinationValidator.isWildcard(next, straight)) {
                        
                      
                        if (!newCard.getSeed().equals(prev.getSeed())) continue;

                        int vPrev = prev.getNumericalValue();
                        int vNew = newCard.getNumericalValue();
                        
                        if (vPrev == 12 && next.getValue().equals("A") && newCard.getValue().equals("K")) return true;
                   
                        if (vNew == vPrev + 1) return true;
                    }
                }
            }
        }
        return false;
    }
}