package it.unibo.burraco.core.combination;

import it.unibo.burraco.model.card.*;

import java.util.*;

public class CombinationValidator {

    public static boolean isValidCombination(List<Card> cards) {
        if (cards == null || cards.size() < 3) {
        return false;
    }

    // --- 2. LOGICA SET (TRIS) ---
    // Se è un set (stesso valore, semi diversi o uguali)
    if (SetUtils.isValidSet(cards)) {
        // In un set, le uniche wildcards possibili sono Jolly e 2.
        // Poiché non esiste un set di "2 naturali", ogni 2 trovato qui è una matta.
        long wildcardsInSet = cards.stream()
                .filter(c -> c.getValue().equalsIgnoreCase("Jolly") || c.getValue().equals("2"))
                .count();
        
        // Regola del Burraco: massimo 1 matta per combinazione
        return wildcardsInSet <= 1;
    }

    // --- 1. LOGICA SCALA (STRAIGHT) ---
    if (StraightUtils.isSameSeed(cards)) {
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
        
        int effectiveWildcards = 0;
        for (int i = 0; i < ordered.size(); i++) {
            Card c = ordered.get(i);
            
            if (c.getValue().equalsIgnoreCase("Jolly")) {
                effectiveWildcards++;
            } else if (c.getValue().equals("2")) {
                // Qui il 2 può essere naturale SE è nella posizione corretta (es. dopo l'Asso o prima del 3)
                if (!isPositionallyNatural(i, ordered)) {
                    effectiveWildcards++;
                }
            }
        }

        if (effectiveWildcards > 1) {
            return false;
        }

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

    // Se siamo in una scala, verifichiamo la posizione
    if (StraightUtils.isSameSeed(context)) {
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(context));
        int index = ordered.indexOf(c);
        return !isPositionallyNatural(index, ordered);
    } 
    
    // Se siamo in un set (Tris), il 2 è SEMPRE una matta.
    return true;
    }
}