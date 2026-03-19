package core.combination;

import model.card.*;

import java.util.*;

public class CombinationValidator {

    public static boolean isValidCombination(List<Card> cards) {
        if (cards == null || cards.size() < 3) {
            return false;
        }

        // --- 2. LOGICA SET (TRIS) ---
        if (SetUtils.isValidSet(cards)) {
            long wildcardsInSet = cards.stream().filter(c -> c.getValue().equalsIgnoreCase("Jolly") || (c.getValue().equals("2") && !SetUtils.isNaturalTwoInSet(c, cards))).count();
            return wildcardsInSet <= 1;
        }

        // --- 1. LOGICA SCALA (STRAIGHT) ---
        if (StraightUtils.isSameSeed(cards)) {
            // Usiamo l'ordinamento reale della scala per vedere dove finisce il 2
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            
            int effectiveWildcards = 0;
            for (int i = 0; i < ordered.size(); i++) {
                Card c = ordered.get(i);
                
                if (c.getValue().equalsIgnoreCase("Jolly")) {
                    effectiveWildcards++;
                } else if (c.getValue().equals("2")) {
                    // Un 2 è naturale SOLO se la sua POSIZIONE nella scala è corretta
                    if (!isPositionallyNatural(i, ordered)) {
                        effectiveWildcards++;
                    }
                }
            }

            // REGOLA BURRACO: Massimo 1 matta (Jolly o 2-matta)
            if (effectiveWildcards > 1) {
                return false;
            }

            // Se il conteggio matte è OK, verifichiamo che la sequenza sia valida (no buchi non coperti)
            return StraightUtils.isValidStraight(cards);
        }

        return false;
    }

    // Fondamentale: controlla se il 2 si trova nel posto riservato al 2 naturale
    private static boolean isPositionallyNatural(int index, List<Card> ordered) {
        
        if (index < 0 || index >= ordered.size()){
            return false;
        } 
        
        Card c = ordered.get(index);
        if (!c.getValue().equals("2")) return false;

        // 1. Caso Classico: [2, 3, 4...] -> Il 2 è all'inizio e dopo c'è il 3
        if (index < ordered.size() - 1) {
            Card next = ordered.get(index + 1);
            if (next.getValue().equals("3") && next.getSeed().equals(c.getSeed())) return true;
        }
        
        // 2. Caso Asso: [A, 2, 3...] -> Il 2 è in seconda posizione tra A e 3
        if (index > 0 && index < ordered.size() - 1) {
             Card prev = ordered.get(index - 1);
             Card next = ordered.get(index + 1);
             if (prev.getValue().equals("A") && next.getValue().equals("3") && prev.getSeed().equals(c.getSeed())) return true;
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
            return !isPositionallyNatural(index, ordered);
        } else {
            return !SetUtils.isNaturalTwoInSet(c, context);
        }
    }
}