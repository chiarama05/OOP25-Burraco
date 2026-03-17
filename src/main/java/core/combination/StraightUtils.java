package core.combination;

import model.card.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Utility class that provides helper methods to validate and order
 * straight (sequence) combinations in card games.
 *
 * This class handles:
 * - Checking if a sequence has all cards of the same suit
 * - Validating a straight including wildcards (Jolly or usable Twos)
 * - Determining if a "2" is natural in a straight
 * - Mapping card values to numeric values for sequence checking
 * - Ordering a straight including wildcards
 */
public class StraightUtils {
    
    /**
     * Checks if all non-wildcard cards in the list belong to the same seed.
     * A straight must contain cards of the same seed.
     *
     * @param cards the list of cards to check
     * @return true if all non-wildcards have the same seed, false otherwise
     */
    public static boolean isSameSeed(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }

        // Filter out wildcards (Jolly or usable 2s)
        List<Card> real = cards.stream().filter(c -> !CombinationValidator.isWildcard(c, cards)).collect(Collectors.toList());

        if (real.isEmpty()) {
            return false;
        }

        // Get seed of first real card
        String suit = real.get(0).getSeed();
        // Check that all real cards have the same seed
        return real.stream().allMatch(c -> c.getSeed().equals(suit));
    }


    public static boolean isValidStraight(List<Card> cards) {
    if (cards == null || cards.size() < 3){
        return false;
    }

    // --- TENTATIVO 1: Considera i 2 come potenziali carte naturali ---
    // (Un 2 è naturale solo se è dello stesso seme delle altre e c'è un A o un 3)
    if (checkLogic(cards, false)){
        return true;
    } 

    // --- TENTATIVO 2: Considera i 2 (anche dello stesso seme) come MATTE ---
    // Questo risolve il caso 3-2-5 dove il 2 deve "fare il 4"
    if (checkLogic(cards, true)){
        return true;
    } 

    return false;
}

private static boolean checkLogic(List<Card> cards, boolean forceTwosAsWildcards) {
    List<Card> real = new ArrayList<>();
    int wildcards = 0;

    for (Card c : cards) {
        if (c.getValue().equals("Jolly")) {
            wildcards++;
        } else if (c.getValue().equals("2")) {
            // Se forziamo i 2 come matte O se il 2 è di seme diverso, è una matta
            if (forceTwosAsWildcards || !isSameSeedAsRest(c, cards)) {
                wildcards++;
            } else {
                // Altrimenti lo trattiamo come naturale (valore 2)
                real.add(c);
            }
        } else {
            real.add(c);
        }
    }

    // Regola del Burraco/Scala: massimo 1 matta (Jolly o un 2 usato come matta)
    if (wildcards > 1){
        return false;
    } 

    if (real.isEmpty()){
        return false;
    } 

    // Test con Asso basso (1) e Asso alto (14)
    List<Integer> aceLow = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
    List<Integer> aceHigh = real.stream().map(c -> mapValue(c, false)).sorted().collect(Collectors.toList());

    return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
}

private static boolean isSameSeedAsRest(Card two, List<Card> cards) {
    return cards.stream().filter(c -> !c.getValue().equals("Jolly") && !c.getValue().equals("2")).findFirst().map(firstReal -> firstReal.getSeed().equals(two.getSeed())).orElse(true);
    }


   public static boolean isNaturalTwo(Card two, List<Card> straight) {
    if (!two.getValue().equals("2")){
        return false;
    } 
    
    String suit = two.getSeed();
    // Un 2 è naturale solo se nella lista ci sono l'Asso o il 3 di quel seme
    boolean hasAce = straight.stream().anyMatch(c -> c.getValue().equals("A") && c.getSeed().equals(suit));
    boolean hasThree = straight.stream().anyMatch(c -> c.getValue().equals("3") && c.getSeed().equals(suit));
    
    return hasAce || hasThree;
}


    /**
     * Maps a card to its numeric value.
     * If aceLow is true, Ace = 1; otherwise Ace = 14.
     *
     * @param c the card to map
     * @param aceLow whether Ace should be considered low (1)
     * @return numeric value of the card
     */
    private static int mapValue(Card c, boolean aceLow) {
        if (c.getValue().equals("A")){
            return aceLow ? 1 : 14;
        } 
        return c.getNumericalValue();
    }

    /**
     * Checks if a sorted list of numeric values can become sequential
     * using the given number of wildcards to fill gaps.
     *
     * @param values sorted list of numeric values of cards
     * @param wildcards number of available wildcards to fill missing numbers
     * @return true if the values can form a sequence, false otherwise
     */
    private static boolean canBeSequential(List<Integer> values, long wildcards) {
        if (values.size() < 2) {
            return true;
        }
        
        // Check for duplicates
        Set<Integer> set = new HashSet<>(values);
        if (set.size() != values.size()) {
            return false; // duplicates make a sequence invalid
        }

        int usedWildcards = 0;

        for (int i = 0; i < values.size() - 1; i++) {
            int gap = values.get(i + 1) - values.get(i) - 1;
            if (gap < 0) {
                return false;
            }
            usedWildcards += gap;
            if (usedWildcards > wildcards) {
                return false;
            }
        }
        
        return true;
    }

    public static List<Card> orderStraight(List<Card> sequence) {
    if (sequence == null || sequence.isEmpty()){
        return new ArrayList<>();
    } 

    // 1. Separiamo le carte reali dalle matte
    List<Card> realCards = sequence.stream().filter(c -> !CombinationValidator.isWildcard(c, sequence)).collect(Collectors.toList());
    
    List<Card> wildcards = sequence.stream().filter(c -> CombinationValidator.isWildcard(c, sequence)).collect(Collectors.toList());

    if (realCards.isEmpty()) return new ArrayList<>(sequence);

    // 2. Determiniamo se l'asso è basso o alto per l'ordinamento
    boolean useAceLow = decideIfAceIsLow(realCards, wildcards.size());
    realCards.sort(Comparator.comparingInt(c -> mapValue(c, useAceLow)));

    List<Card> result = new ArrayList<>();
    int wildUsed = 0;

    // 3. COSTRUZIONE DELLA SCALA E RIEMPIMENTO BUCHI
    for (int i = 0; i < realCards.size(); i++) {
        result.add(realCards.get(i));
        
        if (i < realCards.size() - 1) {
            int v1 = mapValue(realCards.get(i), useAceLow);
            int v2 = mapValue(realCards.get(i + 1), useAceLow);
            int gap = v2 - v1 - 1;

            // Se c'è un buco (es. tra 3 e 5) e abbiamo una matta, la mettiamo in mezzo
            while (gap > 0 && wildUsed < wildcards.size()) {
                result.add(wildcards.get(wildUsed++));
                gap--;
            }
        }
    }

    // 4. GESTIONE MATTE RIMANENTI (se non servono nei buchi, vanno ai bordi)
    while (wildUsed < wildcards.size()) {
        Card w = wildcards.get(wildUsed++);
        // Se c'è spazio "sotto" (es. scala inizia dal 3, mettiamo la matta per fare il 2)
        int firstVal = mapValue(result.get(0), useAceLow);
        if (firstVal > 1) {
            result.add(0, w);
        } else {
            // Altrimenti la mettiamo in coda (sopra il Re o l'Asso alto)
            result.add(w);
        }
    }

    return result;
}

// Metodo di supporto per decidere l'Asso
private static boolean decideIfAceIsLow(List<Card> real, int wildCount) {
    List<Integer> lowVals = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
    if (canBeSequential(lowVals, wildCount)) {
        // Se ho un Re, l'asso è quasi certamente alto (tranne scale lunghissime)
        if (real.stream().anyMatch(c -> c.getValue().equals("K"))){
            return false;
        } 
        return true;
    }
    return false;
}
}



