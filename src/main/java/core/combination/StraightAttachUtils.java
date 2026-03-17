package core.combination;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class StraightAttachUtils {

   public static boolean canAttachToStraight(List<Card> straight, Card newCard) {
       
        List<Card> potentialStraight = new ArrayList<>(straight);
        potentialStraight.add(newCard);

        //if the scale is valid, you can attach (management of 2 and wildcards)
        if (StraightUtils.isValidStraight(potentialStraight)) {
            return true;
        }

        //if the attach is not on the edges, verify if a wildcards is been replaced inside the scale 
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

                    //verify that the new card is the correct one
                    if (!CombinationValidator.isWildcard(prev, straight) && !CombinationValidator.isWildcard(next, straight)) {
                        if (!newCard.getSeed().equals(prev.getSeed())) continue;

                        int vPrev = prev.getNumericalValue();
                        int vNew = newCard.getNumericalValue();
                        
                       //special management of the aces 
                        if (vPrev == 12 && next.getValue().equals("A") && newCard.getValue().equals("K")){
                            return true;
                        } 
                        if (vNew == vPrev + 1){
                            return true;
                        } 
                    }
                }
            }
        }
        return false;
    }
}