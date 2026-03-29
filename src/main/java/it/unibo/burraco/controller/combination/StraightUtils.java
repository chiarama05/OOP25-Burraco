package it.unibo.burraco.controller.combination;

import it.unibo.burraco.model.card.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class that provides helper methods to validate and order
 * straight (sequence) combinations in card games.
 *
 * This class handles:
 * - Checking if a sequence has all cards of the same seed.
 * - Validating a straight including wildcards (jolly/2).
 * - Determining if a "2" is natural in a straight based on its position.
 * - Mapping card values to numeric values for sequence checking.
 * - Ordering a straight including wildcards.
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

        // Filter out Jolly and 2 to find the "natural" cards of the straight
        List<Card> pureReal = cards.stream()
                .filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2"))
                .collect(Collectors.toList());

        if (pureReal.isEmpty()){
            return true;
        } 

        String suit = pureReal.get(0).getSeed();
        return pureReal.stream().allMatch(c -> c.getSeed().equals(suit));
    }

    /**
     * Validates if a list of cards forms a legal straight.
     *
     * @param cards the list of cards to validate
     * @return true if the cards form a valid sequence, false otherwise
     */
    public static boolean isValidStraight(List<Card> cards) {
        if (cards == null || cards.size() < 3){
            return false;
        }
        if (checkLogic(cards, false)){
            return true;
        } 
        if (checkLogic(cards, true)){
            return true;
        } 
        return false;
    }

    /**
     * Internal logic to check sequence validity.
     * 
     * @param cards the list of cards
     * @param forceTwosAsWildcards if true, any '2' is treated as a wildcard
     * @return true if the combination is mathematically sequential
     */
    private static boolean checkLogic(List<Card> cards, boolean forceTwosAsWildcards) {
        List<Card> real = new ArrayList<>();
        int wildcards = 0;
        boolean naturalTwoUsed = false;
        for (Card c : cards) {
            if (c.getValue().equals("Jolly")) {
                wildcards++;
            } else if (c.getValue().equals("2")) {
                if (!forceTwosAsWildcards && !naturalTwoUsed && isSameSeedAsRest(c, cards)) {
                    real.add(c);
                    naturalTwoUsed = true;
                } else {
                    wildcards++;
                }
            } else {
                real.add(c);
            }
        }

        // Burraco rules: max 1 wildcard per combination
        if (wildcards > 1 || real.isEmpty()) {
            return false;
        }

        // Check if all natural cards share the same seed
        String referenceSuit = real.get(0).getSeed();
        for (Card c : real) {
            if (!c.getSeed().equals(referenceSuit)) {
                return false;
            }
        }

        List<Integer> aceLow = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        List<Integer> aceHigh = real.stream().map(c -> mapValue(c, false)).sorted().collect(Collectors.toList());
        return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
    }

    /**
     * Checks whether a '2' card shares the same seed as the first non-wildcard,
     * non-2 card found in the list. Used to decide if the 2 can act as a natural card.
     *
     * @param two   the '2' card to evaluate
     * @param cards the full list of cards in the combination
     * @return true if the suits match or no reference card exists, false otherwise
     */
    private static boolean isSameSeedAsRest(Card two, List<Card> cards) {
        return cards.stream()
            .filter(c -> !c.getValue().equals("Jolly") && !c.getValue().equals("2"))
            .findFirst()
            .map(firstReal -> firstReal.getSeed().equals(two.getSeed()))
            .orElse(true);
    }

    /**
     * Determines if a '2' card is natural in a straight by checking whether
     * an Ace or a 3 of the same seed is present in the sequence.
     * A natural 2 acts as a regular ranked card (rank 2), not as a wildcard.
     *
     * @param two      the card to evaluate
     * @param straight the straight in which the card appears
     * @return true if the 2 is natural, false otherwise
     */
    public static boolean isNaturalTwo(Card two, List<Card> straight) {
        if (!two.getValue().equals("2")) {
            return false;
        }
        String suit = two.getSeed();
        boolean hasAce = straight.stream().anyMatch(c -> c.getValue().equals("A") && c.getSeed().equals(suit));
        boolean hasThree = straight.stream().anyMatch(c -> c.getValue().equals("3") && c.getSeed().equals(suit));
        return hasAce || hasThree;
    }


    /**
     * Maps a card to its numeric value for sequence comparison.
     * Ace is the only card with a dual value depending on context.
     *
     * @param c      the card to map
     * @param aceLow if true, Ace is treated as 1 (low); if false, Ace is treated as 14 (high)
     * @return the numeric value of the card
     */
    private static int mapValue(Card c, boolean aceLow) {
        if (c.getValue().equals("A")) return aceLow ? 1 : 14;
        return c.getNumericalValue();
    }

    /**
     * Checks whether a sorted list of numeric values can form a consecutive sequence
     * using the available wildcards to fill any gaps.
     * Duplicate values are not allowed and immediately return false.
     *
     * @param values    sorted list of numeric card values
     * @param wildcards number of wildcards available to fill gaps
     * @return true if the values can be made sequential, false otherwise
     */
    private static boolean canBeSequential(List<Integer> values, long wildcards) {
        if (values.size() < 2) {
            return true;
        }
        // No duplicates allowed in a straight
        Set<Integer> set = new HashSet<>(values);
        if (set.size() != values.size()) {
            return false;
        }

        int totalGap = 0;
        for (int i = 0; i < values.size() - 1; i++) {
            int gap = values.get(i + 1) - values.get(i) - 1;
            if (gap < 0) {
                return false;
            }
            totalGap += gap;
        }
        return totalGap <= wildcards;
    }

    /**
     * Orders a list of cards into a valid sequence.
     * 
     * @param sequence the unordered list of cards
     * @return a new list sorted by rank, with wildcards in the correct placeholder positions
     */
    public static List<Card> orderStraight(List<Card> sequence) {
        if (sequence == null || sequence.isEmpty()) {
            return new ArrayList<>();
        }
        List<Card> attempt = buildOrdering(sequence, false);
        if (attempt != null) {
            return attempt;
        }
        attempt = buildOrdering(sequence, true);
        if (attempt != null) {
            return attempt;
        }
        return new ArrayList<>(sequence);
    }


    /**
     * Attempts to build an ordered sequence from the given cards.
     * Natural cards are sorted by rank; wildcards are inserted into gaps.
     * Any remaining wildcard after gap-filling is placed at the nearest open border.
     *
     * @param sequence         the unordered list of cards
     * @param forceTwosAsWild  if true, all 2s are treated as wildcards regardless of suit
     * @return the ordered list, or null if no valid ordering exists
     */
    private static List<Card> buildOrdering(List<Card> sequence, boolean forceTwosAsWild) {
        List<Card> wilds = new ArrayList<>();
        List<Card> real  = new ArrayList<>();
        boolean naturalTwoUsed = false;
        for (Card c : sequence) {
            if (c.getValue().equalsIgnoreCase("Jolly")) {
                wilds.add(c);
            } else if (c.getValue().equals("2")) {
                if (!forceTwosAsWild && !naturalTwoUsed && isSameSeedAsRest(c, sequence)) {
                    real.add(c);
                    naturalTwoUsed = true;
                } else {
                    wilds.add(c);
                }
            } else {
                real.add(c);
            }
        }

        if (wilds.size() > 1 || real.isEmpty()) {
            return null;
        }

        String suit = real.get(0).getSeed();
        if (!real.stream().allMatch(c -> c.getSeed().equals(suit))) {
            return null;
        }

        boolean aceLow = decideIfAceIsLow(real, wilds.size());
        real.sort(Comparator.comparingInt(c -> mapValue(c, aceLow)));

        // Calculate gaps between natural cards
        int totalGap = 0;
        for (int i = 0; i < real.size() - 1; i++) {
            int gap = mapValue(real.get(i + 1), aceLow) - mapValue(real.get(i), aceLow) - 1;
            if (gap < 0) {
                return null;
            }
            totalGap += gap;
        }
        if (totalGap > wilds.size()) {
            return null;
        }

        // Build the result by interleaving natural cards and wildcards
        List<Card> wildsCopy = new ArrayList<>(wilds);
        List<Card> result = new ArrayList<>();
        for (int i = 0; i < real.size(); i++) {
            result.add(real.get(i));
            if (i < real.size() - 1) {
                int gap = mapValue(real.get(i + 1), aceLow) - mapValue(real.get(i), aceLow) - 1;
                while (gap > 0 && !wildsCopy.isEmpty()) {
                    result.add(wildsCopy.remove(0));
                    gap--;
                }
            }
        }

        // Place remaining wildcard at the end or beginning
        while (!wildsCopy.isEmpty()) {
            Card w = wildsCopy.remove(0);
            int lastVal  = mapValue(result.get(result.size() - 1), aceLow);
            int firstVal = mapValue(result.get(0), aceLow);
            if (lastVal < 14) {
                result.add(w);
            } else if (firstVal > 1) {
                result.add(0, w);
            } else {
                result.add(w);
            }        
        }
        return result;
    }

    /**
     * Determines whether the Ace should be treated as low (value 1) or high (value 14)
     * when ordering a straight.
     *
     * @param real      the list of natural (non-wildcard) cards in the straight
     * @param wildCount the number of wildcards available to fill gaps
     * @return true if the Ace should be treated as 1 (low), false if it should be 14 (high)
     */
    private static boolean decideIfAceIsLow(List<Card> real, int wildCount) {
        List<Integer> lowVals = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        if (canBeSequential(lowVals, wildCount)) {
            if (real.stream().anyMatch(c -> c.getValue().equals("K"))) return false;
            return true;
        }
        return false;
    }

    /**
     * Verifies if a card at a specific index in an ordered list is a "natural" Two
     * based on its mathematical position relative to an anchor card.
     * @param index the position to check
     * @param ordered the ordered list of cards
     * @return true if the '2' at that position is natural, false otherwise
     */
    public static boolean isPositionallyNatural(int index, List<Card> ordered) {
        if (index < 0 || index >= ordered.size()) {
            return false;
        }
        Card c = ordered.get(index);
        if (!c.getValue().equals("2")) {
            return false;
        }

        // Use a natural card (not Joker, not 2) as an anchor to calculate expected values
        for (int i = 0; i < ordered.size(); i++) {
            Card anchor = ordered.get(i);
            if (anchor.getValue().equalsIgnoreCase("Jolly") || anchor.getValue().equals("2")) continue;

            if (anchor.getValue().equals("A")) {
                if (1  + (index - i) == 2) return true;
                if (14 + (index - i) == 2) return true;
                continue;
            }
            return anchor.getNumericalValue() + (index - i) == 2;
        }
        return false;
    }
}



