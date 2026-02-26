package core.combination;

import model.card.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class that provides helper methods to validate and order
 * straight (sequence) combinations in the game.
 */
public class StraightUtils {
    
     /**
     * Checks if all non-wildcard cards belong to the same suit.
     * A straight must contain cards of the same suit.
     */
    public static boolean isStraight(List<Card> cards) {
        List<Card> real = cards.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, cards))
                .collect(Collectors.toList()); 

        if (real.isEmpty()) {
            return false;
        }

        String suit = real.get(0).getSeed();
        return real.stream().allMatch(c -> c.getSeed().equals(suit));
    }


    /**
     * Verifies if the given cards can form a valid straight.
     * Wildcards (Jolly or usable Twos) are considered to fill gaps.
     * The Ace can be treated as low (1) or high (14).
     */
    public static boolean isValidStraight(List<Card> cards) {
        List<Card> real = cards.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, cards))
                .collect(Collectors.toList());

        if (real.isEmpty()) {
            return false;
        } 

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

        return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
    }

    /**
     * Determines whether a "2" card is natural inside a straight.
     * A Two is natural only if it fits between Ace and Three
     * and has the same suit as the other real cards.
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

        String suit = real.get(0).getSeed();
        if (!two.getSeed().equals(suit)) return false;

        boolean hasAce = real.stream().anyMatch(c -> c.getValue().equals("A"));
        boolean hasThree = real.stream().anyMatch(c -> c.getValue().equals("3"));

        return hasAce && hasThree;
    }


    /**
     * Maps a card to its numeric value.
     * If aceLow is true, Ace = 1.
     * Otherwise, Ace = 14.
     */
    private static int mapValue(Card c, boolean aceLow) {
        if (c.getValue().equals("A")) return aceLow ? 1 : 14;
        return c.getNumericalValue();
    }

    /**
     * Checks if a sorted list of values can become sequential
     * using the given number of wildcards to fill gaps.
     */
    private static boolean canBeSequential(List<Integer> values, long wildcards) {
        if (values.size() < 2) {
            return true;
        }
        
        Set<Integer> set = new HashSet<>(values);
       
        if (set.size() != values.size()) {
            return false;
        }

        Collections.sort(values);
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



