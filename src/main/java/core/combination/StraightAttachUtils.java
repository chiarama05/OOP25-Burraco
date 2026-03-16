package core.combination;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class StraightAttachUtils {

   public static boolean canAttachToStraight(List<Card> straight, Card newCard) {
        // --- 1. TEST DI VALIDITÀ GENERALE (Simulazione) ---
        // Creiamo una lista temporanea con la nuova carta
        List<Card> potentialStraight = new ArrayList<>(straight);
        potentialStraight.add(newCard);

        // Se la scala risultante è valida (gestendo 2 naturali/matte), allora puoi attaccare
        if (StraightUtils.isValidStraight(potentialStraight)) {
            return true;
        }

        // --- 2. LOGICA DI SOSTITUZIONE MATTA INTERNA ---
        // Se non è un attacco ai bordi, verifichiamo se stiamo rimpiazzando un Jolly/2-matta 
        // che si trova "in mezzo" alla scala.
        return canSubstituteInternalWildcard(straight, newCard);
    }

    private static boolean canSubstituteInternalWildcard(List<Card> straight, Card newCard) {
        List<Card> ord = StraightUtils.orderStraight(straight);
        
        for (int i = 0; i < ord.size(); i++) {
            Card current = ord.get(i);
            
            // Se la carta nella scala è una matta (Jolly o 2 usato come tale)
            if (CombinationValidator.isWildcard(current, straight)) {
                // Verifichiamo se è in una posizione interna (non ai bordi)
                if (i > 0 && i < ord.size() - 1) {
                    Card prev = ord.get(i - 1);
                    Card next = ord.get(i + 1);

                    // Se le carte vicine sono reali, verifichiamo se la nuova carta "tappa il buco"
                    if (!CombinationValidator.isWildcard(prev, straight) && 
                        !CombinationValidator.isWildcard(next, straight)) {
                        
                        // Devono avere lo stesso seme
                        if (!newCard.getSeed().equals(prev.getSeed())) continue;

                        int vPrev = prev.getNumericalValue();
                        int vNew = newCard.getNumericalValue();
                        
                        // Gestione speciale Asso: se rimpiazzo Jolly in Q - Jolly - A
                        if (vPrev == 12 && next.getValue().equals("A") && newCard.getValue().equals("K")) return true;
                        // Caso normale: 4 - Jolly - 6 -> inserisco 5
                        if (vNew == vPrev + 1) return true;
                    }
                }
            }
        }
        return false;
    }
}