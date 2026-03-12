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
        List<Card> real = cards.stream()
            .filter(c -> !CombinationValidator.isWildcard(c, cards))
            .collect(Collectors.toList());

        if (real.isEmpty()) {
            return false;
        }

        // Get seed of first real card
        String suit = real.get(0).getSeed();
        // Check that all real cards have the same seed
        return real.stream().allMatch(c -> c.getSeed().equals(suit));
    }


    /**
     * Verifies if the given list of cards can form a valid straight.
     * Wildcards (Jolly or usable Twos) can fill gaps in the sequence.
     * The Ace can be considered as low (1) or high (14).
     *
     * @param cards the list of cards to check
     * @return true if the cards can form a valid straight, false otherwise
     */
    public static boolean isValidStraight(List<Card> cards) {
        List<Card> real = cards.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, cards))
                .collect(Collectors.toList());

        if (real.isEmpty()) {
            return false;
        } 

        // Map real cards to numeric values (Ace as low and high)
        List<Integer> aceLow = real.stream()
                .map(c -> mapValue(c, true))
                .sorted()
                .collect(Collectors.toList());

        List<Integer> aceHigh = real.stream()
                .map(c -> mapValue(c, false))
                .sorted()
                .collect(Collectors.toList());

        long wildcards = cards.stream()
                .filter(c -> CombinationValidator.isWildcard(c, cards))
                .count();

        // Check if either low-Ace or high-Ace sequence is valid
        return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
    }

    /**
     * Determines if a "2" card is natural in a straight (A-2-3).
     * A natural two is not considered a wildcard.
     *
     * @param two the 2 card to check
     * @param straight the list of cards forming the straight
     * @return true if the 2 is natural, false otherwise
     */
    public static boolean isNaturalTwo(Card two, List<Card> straight) {
        if (!two.getValue().equals("2")) {
            return false;
        }

        List<Card> real = straight.stream()
                .filter(c -> !c.getValue().equals("2") && !c.getValue().equals("Jolly"))
                .collect(Collectors.toList());

        if (real.isEmpty()) {
            return false;
        }

        // Natural two must have the same seed as the other real cards
        String seed = real.get(0).getSeed();
        if (!two.getSeed().equals(seed)) return false;

        // Check for presence of Ace and Three to form natural A-2-3
        boolean hasAce = real.stream().anyMatch(c -> c.getValue().equals("A"));
        boolean hasThree = real.stream().anyMatch(c -> c.getValue().equals("3"));

        return hasAce && hasThree;
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
        if (c.getValue().equals("A")) return aceLow ? 1 : 14;
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
    List<Card> real = sequence.stream()
            .filter(c -> !CombinationValidator.isWildcard(c, sequence))
            .collect(Collectors.toList());
    List<Card> wild = sequence.stream()
            .filter(c -> CombinationValidator.isWildcard(c, sequence))
            .collect(Collectors.toList());

    if (real.isEmpty()) return new ArrayList<>(sequence);

    // Determiniamo se l'asso è basso (1) o alto (14)
    List<Integer> aceLowVals = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
    List<Integer> aceHighVals = real.stream().map(c -> mapValue(c, false)).sorted().collect(Collectors.toList());

    // Se entrambi sono validi, preferiamo l'asso basso se c'è un 2 o 3, alto se c'è un K o Q
    boolean useAceLow = canBeSequential(aceLowVals, wild.size());
    if (useAceLow && canBeSequential(aceHighVals, wild.size())) {
        // Se ho sia A che K, l'asso è sicuramente alto
        if (real.stream().anyMatch(c -> c.getValue().equals("K"))) useAceLow = false;
    }

    final boolean finalUseAceLow = useAceLow;
    real.sort(Comparator.comparingInt(c -> mapValue(c, finalUseAceLow)));

    List<Card> result = new ArrayList<>();
    int wildIndex = 0;

    // Ricostruzione con gestione gap
    for (int i = 0; i < real.size(); i++) {
        result.add(real.get(i));
        if (i < real.size() - 1) {
            int v1 = mapValue(real.get(i), finalUseAceLow);
            int v2 = mapValue(real.get(i+1), finalUseAceLow);
            int gap = v2 - v1 - 1;
            while (gap-- > 0 && wildIndex < wild.size()) {
                result.add(wild.get(wildIndex++));
            }
        }
    }

    // Gestione Matte rimanenti (Jolly o 2 liberi)
    // Se la sequenza finisce con l'Asso basso (1), la matta non può stare prima.
    // Se la sequenza inizia con 3, 2, la matta può stare in cima (Asso).
    // ... (codice precedente di orderStraight)

    // Gestione Matte rimanenti
    while (wildIndex < wild.size()) {
        Card w = wild.get(wildIndex++);
        // Se l'asso è basso (1, 2, 3...), la matta NON può andare prima dell'Asso.
        // La mettiamo in fondo, a meno che non manchi il 2 nella scala A-3
        if (finalUseAceLow && real.get(0).getValue().equals("A") && real.size() > 1 && mapValue(real.get(1), true) == 3) {
             result.add(1, w); // Metti il jolly in posizione del '2'
        } else {
             result.add(w); // Default: in fondo
        }
    }
    return result;
}
}



