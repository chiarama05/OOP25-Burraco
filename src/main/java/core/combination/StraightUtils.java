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
    public static boolean isSameSuit(List<Card> cards) {
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

    /**
     * Orders a valid straight by placing real cards in sequence
     * and inserting wildcards where necessary to fill gaps.
     *
     * @param sequence list of cards forming a straight (may include wildcards)
     * @return a new list of cards ordered as a valid straight
     */
    public static List<Card> orderStraight(List<Card> sequence) {
        
        List<Card> real = sequence.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, sequence))
                .collect(Collectors.toList());

        List<Card> wild = sequence.stream()
                .filter(c -> CombinationValidator.isWildcard(c, sequence))
                .collect(Collectors.toList());

        if (real.isEmpty()) {
            return new ArrayList<>(sequence);
        }

        List<Integer> aceLow = real.stream().map(c -> mapValue(c, true)).collect(Collectors.toList());
        List<Integer> aceHigh = real.stream().map(c -> mapValue(c, false)).collect(Collectors.toList());

        boolean useAceLow = canBeSequential(aceLow, wild.size());

        List<Integer> usedValues = useAceLow ? aceLow : aceHigh;

        Map<Integer, Card> map = new HashMap<>();
        for (Card c : real) {
            map.put(mapValue(c, useAceLow), c);
        }

        Collections.sort(usedValues);

        List<Card> result = new ArrayList<>();
        int wildIndex = 0;

        // Reconstruct the straight, filling gaps with wildcards
        for (int i = 0; i < usedValues.size() - 1; i++) {
            int v1 = usedValues.get(i);
            int v2 = usedValues.get(i + 1);
            result.add(map.get(v1));
            int gap = v2 - v1 - 1;

            while (gap-- > 0 && wildIndex < wild.size()) {
                result.add(wild.get(wildIndex++));
            }
        }

        result.add(map.get(usedValues.get(usedValues.size() - 1)));

        while (wildIndex < wild.size()) {
            result.add(wild.get(wildIndex++));
        }

        return result;
    }
}



