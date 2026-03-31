package it.unibo.burraco.controller.combination.straight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.burraco.model.card.Card;

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
public final class StraightUtils {

    private static final String JOLLY = "Jolly";
    private static final String TWO = "2";
    private static final String ACE = "A";
    private static final String THREE = "3";
    private static final String KING = "K";

    private static final int ACE_LOW_VALUE = 1;
    private static final int ACE_HIGH_VALUE = 14;
    private static final int TWO_VALUE = 2;
    private static final int MIN_STRAIGHT_SIZE = 3;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StraightUtils() { }

    /**
     * Checks if all non-wildcard cards in the list belong to the same seed.
     * 
     * @param cards the list of cards to check
     * @return true if all non-wildcards have the same seed, false otherwise
     */
    public static boolean isSameSeed(final List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }

        final List<Card> pureReal = cards.stream()
                .filter(c -> !JOLLY.equalsIgnoreCase(c.getValue()) && !TWO.equals(c.getValue()))
                .collect(Collectors.toList());

        if (pureReal.isEmpty()) {
            return true;
        }

        final String seed = pureReal.get(0).getSeed();
        return pureReal.stream().allMatch(c -> c.getSeed().equals(seed));
    }

    /**
     * Validates if a list of cards forms a legal straight.
     * 
     * @param cards the list of cards to validate
     * @return true if the cards form a valid sequence, false otherwise
     */
    public static boolean isValidStraight(final List<Card> cards) {
        if (cards == null || cards.size() < MIN_STRAIGHT_SIZE) {
            return false;
        }
        return checkLogic(cards, false) || checkLogic(cards, true);
    }

    /**
     * Internal logic to check sequence validity.
     * 
     * @param cards the list of cards
     * @param forceTwosAsWildcards if true, any '2' is treated as a wildcard
     * @return true if the combination is mathematically sequential
     */
    private static boolean checkLogic(final List<Card> cards, final boolean forceTwosAsWildcards) {
        final List<Card> real = new ArrayList<>();
        int wildcards = 0;
        boolean naturalTwoUsed = false;

        for (final Card c : cards) {
            if (JOLLY.equals(c.getValue())) {
                wildcards++;
            } else if (TWO.equals(c.getValue())) {
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

        if (wildcards > 1 || real.isEmpty()) {
            return false;
        }

        final String referenceSuit = real.get(0).getSeed();
        for (final Card c : real) {
            if (!c.getSeed().equals(referenceSuit)) {
                return false;
            }
        }

        final List<Integer> aceLow = real.stream()
                .map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        final List<Integer> aceHigh = real.stream()
                .map(c -> mapValue(c, false)).sorted().collect(Collectors.toList());
        return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
    }

    /**
     * Checks whether a '2' card shares the same seed as the first non-wildcard card in the list.
     * 
     * @param two   the '2' card to evaluate
     * @param cards the full list of cards in the combination
     * @return true if the suits match or no reference card exists, false otherwise
     */
    private static boolean isSameSeedAsRest(final Card two, final List<Card> cards) {
        return cards.stream()
                .filter(c -> !JOLLY.equals(c.getValue()) && !TWO.equals(c.getValue()))
                .findFirst()
                .map(firstReal -> firstReal.getSeed().equals(two.getSeed()))
                .orElse(true);
    }

    /**
     * Determines if a '2' card is natural in a straight by checking whether
     * an Ace or a 3 of the same seed is present in the sequence.
     * 
     * @param two      the card to evaluate
     * @param straight the straight in which the card appears
     * @return true if the 2 is natural, false otherwise
     */
    public static boolean isNaturalTwo(final Card two, final List<Card> straight) {
        if (!TWO.equals(two.getValue())) {
            return false;
        }
        String suit = two.getSeed();
        final boolean hasAce = straight.stream()
                .anyMatch(c -> ACE.equals(c.getValue()) && c.getSeed().equals(suit));
        final boolean hasThree = straight.stream()
                .anyMatch(c -> THREE.equals(c.getValue()) && c.getSeed().equals(suit));
        return hasAce || hasThree;
    }

    /**
     * Maps a card to its numeric value for sequence comparison.
     * 
     * @param c      the card to map
     * @param aceLow if true, Ace is treated as 1 (low); if false, Ace is treated as 14 (high)
     * @return the numeric value of the card
     */
    private static int mapValue(final Card c, final boolean aceLow) {
        if (ACE.equals(c.getValue())) {
            return aceLow ? ACE_LOW_VALUE : ACE_HIGH_VALUE;
        }
        return c.getNumericalValue();
    }

    /**
     * Checks whether a sorted list of numeric values can form a consecutive sequence
     * using the available wildcards to fill any gaps.
     * 
     * @param values    sorted list of numeric card values
     * @param wildcards number of wildcards available to fill gaps
     * @return true if the values can be made sequential, false otherwise
     */
    private static boolean canBeSequential(final List<Integer> values, final long wildcards) {
        if (values.size() < 2) {
            return true;
        }
        final Set<Integer> set = new HashSet<>(values);
        if (set.size() != values.size()) {
            return false;
        }

        int totalGap = 0;
        for (int i = 0; i < values.size() - 1; i++) {
            final int gap = values.get(i + 1) - values.get(i) - 1;
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
    public static List<Card> orderStraight(final List<Card> sequence) {
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
     * 
     * @param sequence the unordered list of cards
     * @param forceTwosAsWild if true, all 2s are treated as wildcards regardless of suit
     * @return the ordered list, or null if no valid ordering exists
     */
    private static List<Card> buildOrdering(final List<Card> sequence, final boolean forceTwosAsWild) {
        final List<Card> wilds = new ArrayList<>();
        final List<Card> real = new ArrayList<>();
        boolean naturalTwoUsed = false;

        for (final Card c : sequence) {
            if (JOLLY.equalsIgnoreCase(c.getValue())) {
                wilds.add(c);
            } else if (TWO.equals(c.getValue())) {
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

        final String suit = real.get(0).getSeed();
        if (!real.stream().allMatch(c -> c.getSeed().equals(suit))) {
            return null;
        }

        final boolean aceLow = decideIfAceIsLow(real, wilds.size());
        real.sort(Comparator.comparingInt(c -> mapValue(c, aceLow)));

        int totalGap = 0;
        for (int i = 0; i < real.size() - 1; i++) {
            final int gap = mapValue(real.get(i + 1), aceLow) - mapValue(real.get(i), aceLow) - 1;
            if (gap < 0) {
                return null;
            }
            totalGap += gap;
        }
        if (totalGap > wilds.size()) {
            return null;
        }

        final List<Card> wildsCopy = new ArrayList<>(wilds);
        final List<Card> result = new ArrayList<>();
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

        while (!wildsCopy.isEmpty()) {
            final Card w = wildsCopy.remove(0);
            final int lastVal = mapValue(result.get(result.size() - 1), aceLow);
            final int firstVal = mapValue(result.get(0), aceLow);
            if (lastVal < ACE_HIGH_VALUE) {
                result.add(w);
            } else if (firstVal > ACE_LOW_VALUE) {
                result.add(0, w);
            } else {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Determines whether the Ace should be treated as low (value 1) or high (value 14).
     * 
     * @param real the list of natural cards in the straight
     * @param wildCount the number of wildcards available
     * @return true if the Ace should be treated as 1 (low), false if 14 (high)
     */
    private static boolean decideIfAceIsLow(final List<Card> real, final int wildCount) {
        final List<Integer> lowVals = real.stream()
                .map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        if (canBeSequential(lowVals, wildCount)) {
            return !real.stream().anyMatch(c -> "K".equals(c.getValue()));
        }
        return false;
    }

    /**
     * Verifies if a card at a specific index in an ordered list is a natural Two
     * based on its mathematical position relative to an anchor card.
     * 
     * @param index   the position to check
     * @param ordered the ordered list of cards
     * @return true if the '2' at that position is natural, false otherwise
     */
    public static boolean isPositionallyNatural(final int index, final List<Card> ordered) {
        if (index < 0 || index >= ordered.size()) {
            return false;
        }
        final Card c = ordered.get(index);
        if (!TWO.equals(c.getValue())) {
            return false;
        }

        for (int i = 0; i < ordered.size(); i++) {
            final Card anchor = ordered.get(i);
            if (JOLLY.equalsIgnoreCase(anchor.getValue()) || TWO.equals(anchor.getValue())) {
                continue;
            }
            if (ACE.equals(anchor.getValue())) {
                if (ACE_LOW_VALUE + (index - i) == TWO_VALUE || ACE_HIGH_VALUE + (index - i) == TWO_VALUE) {
                    return true;
                }
                continue;
            }
            return anchor.getNumericalValue() + (index - i) == TWO_VALUE;
        }
        return false;
    }
}
