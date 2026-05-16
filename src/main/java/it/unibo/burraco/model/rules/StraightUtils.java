package it.unibo.burraco.model.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;

/**
 * Utility class that provides helper methods to validate and order
 * straight (sequence) combinations in card games.
 *
 * <p>All raw string comparisons have been replaced with
 * {@link CardValue} and {@link Seed} enum references.
 */
public final class StraightUtils {

    private static final int ACE_LOW_VALUE  = 1;
    private static final int ACE_HIGH_VALUE = 14;
    private static final int TWO_VALUE      = 2;
    private static final int MIN_STRAIGHT_SIZE = 3;

    /**
     * Default constructor for instantiation.
     */
    public StraightUtils() {
        // Default constructor
    }

    /**
     * Checks if all non-wildcard cards in the list belong to the same seed.
     *
     * @param cards the list of cards to check
     * @return true if all non-wildcards have the same seed, false otherwise
     */
    public boolean isSameSeed(final List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }

        final List<Card> pureReal = cards.stream()
                .filter(c -> !c.getValue().isPotentialWildcard())
                .collect(Collectors.toList());

        if (pureReal.isEmpty()) {
            return true;
        }

        final Seed seed = pureReal.get(0).getSeed();
        return pureReal.stream().allMatch(c -> c.getSeed() == seed);
    }

    /**
     * Validates if a list of cards forms a legal straight.
     *
     * @param cards the list of cards to validate
     * @return true if the cards form a valid sequence, false otherwise
     */
    public boolean isValidStraight(final List<Card> cards) {
        if (cards == null || cards.size() < MIN_STRAIGHT_SIZE) {
            return false;
        }
        return checkLogic(cards, false) || checkLogic(cards, true);
    }

    /**
     * Internal logic to check sequence validity.
     *
     * @param cards               the list of cards
     * @param forceTwosAsWildcards if true, any Two is treated as a wildcard
     * @return true if the combination is mathematically sequential
     */
    private boolean checkLogic(final List<Card> cards, final boolean forceTwosAsWildcards) {
        final List<Card> real = new ArrayList<>();
        int wildcards = 0;
        boolean naturalTwoUsed = false;

        for (final Card c : cards) {
            if (c.getValue().isJolly()) {
                wildcards++;
            } else if (c.getValue() == CardValue.TWO) {
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

        final Seed referenceSuit = real.get(0).getSeed();
        for (final Card c : real) {
            if (c.getSeed() != referenceSuit) {
                return false;
            }
        }

        final List<Integer> aceLow  = real.stream()
                .map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        final List<Integer> aceHigh = real.stream()
                .map(c -> mapValue(c, false)).sorted().collect(Collectors.toList());
        return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
    }

    /**
     * Checks whether a Two card shares the same seed as the first non-wildcard card.
     *
     * @param two   the Two card to evaluate
     * @param cards the full list of cards in the combination
     * @return true if the suits match, false otherwise
     */
    private boolean isSameSeedAsRest(final Card two, final List<Card> cards) {
        return cards.stream()
                .filter(c -> !c.getValue().isPotentialWildcard())
                .findFirst()
                .map(firstReal -> firstReal.getSeed() == two.getSeed())
                .orElse(true);
    }

    /**
     * Determines if a Two card is natural in a straight by checking whether
     * an Ace or a Three of the same seed is present.
     *
     * @param two      the card to evaluate
     * @param straight the straight in which the card appears
     * @return true if the Two is natural, false otherwise
     */
    public boolean isNaturalTwo(final Card two, final List<Card> straight) {
        if (two.getValue() != CardValue.TWO) {
            return false;
        }
        final Seed suit = two.getSeed();
        final boolean hasAce   = straight.stream()
                .anyMatch(c -> c.getValue() == CardValue.ACE   && c.getSeed() == suit);
        final boolean hasThree = straight.stream()
                .anyMatch(c -> c.getValue() == CardValue.THREE && c.getSeed() == suit);
        return hasAce || hasThree;
    }

    /**
     * Maps a card to its numeric value for sequence comparison.
     *
     * @param c      the card to map
     * @param aceLow if true, Ace is treated as 1; if false, as 14
     * @return the numeric value of the card
     */
    private int mapValue(final Card c, final boolean aceLow) {
        if (c.getValue() == CardValue.ACE) {
            return aceLow ? ACE_LOW_VALUE : ACE_HIGH_VALUE;
        }
        return c.getValue().getNumericalValue();
    }

    /**
     * Checks whether a sorted list of numeric values can form a consecutive sequence
     * using the available wildcards to fill gaps.
     *
     * @param values    sorted list of numeric card values
     * @param wildcards number of wildcards available to fill gaps
     * @return true if the values can be made sequential, false otherwise
     */
    private boolean canBeSequential(final List<Integer> values, final long wildcards) {
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
     * @return a new list sorted by rank, with wildcards in the correct positions
     */
    public List<Card> orderStraight(final List<Card> sequence) {
        if (sequence == null || sequence.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Card> lowAttempt = buildOrdering(sequence, false);
        if (!lowAttempt.isEmpty()) {
            return lowAttempt;
        }
        final List<Card> highAttempt = buildOrdering(sequence, true);
        if (!highAttempt.isEmpty()) {
            return highAttempt;
        }
        return new ArrayList<>(sequence);
    }

    /**
     * Attempts to build an ordered sequence from the given cards.
     *
     * @param sequence        the unordered list of cards
     * @param forceTwosAsWild if true, all Twos are treated as wildcards
     * @return the ordered list, or empty if no valid ordering exists
     */
    private List<Card> buildOrdering(final List<Card> sequence, final boolean forceTwosAsWild) {
        final List<Card> wilds = new ArrayList<>();
        final List<Card> real  = new ArrayList<>();
        boolean naturalTwoUsed = false;

        for (final Card c : sequence) {
            if (c.getValue().isJolly()) {
                wilds.add(c);
            } else if (c.getValue() == CardValue.TWO) {
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
            return Collections.emptyList();
        }

        final Seed suit = real.get(0).getSeed();
        if (!real.stream().allMatch(c -> c.getSeed() == suit)) {
            return Collections.emptyList();
        }

        final boolean aceLow = decideIfAceIsLow(real, wilds.size());
        real.sort(Comparator.comparingInt(c -> mapValue(c, aceLow)));

        int totalGap = 0;
        for (int i = 0; i < real.size() - 1; i++) {
            final int gap = mapValue(real.get(i + 1), aceLow) - mapValue(real.get(i), aceLow) - 1;
            if (gap < 0) {
                return Collections.emptyList();
            }
            totalGap += gap;
        }
        if (totalGap > wilds.size()) {
            return Collections.emptyList();
        }

        final List<Card> wildsCopy = new ArrayList<>(wilds);
        final List<Card> result    = new ArrayList<>();
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
            final Card w       = wildsCopy.remove(0);
            final int lastVal  = mapValue(result.get(result.size() - 1), aceLow);
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
     * Determines whether the Ace should be treated as low (1) or high (14).
     *
     * @param real      the list of natural cards in the straight
     * @param wildCount the number of wildcards available
     * @return true if the Ace should be treated as 1, false if 14
     */
    private boolean decideIfAceIsLow(final List<Card> real, final int wildCount) {
        final List<Integer> lowVals = real.stream()
                .map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        return canBeSequential(lowVals, wildCount)
                && real.stream().noneMatch(c -> c.getValue() == CardValue.KING);
    }

    /**
     * Verifies if a card at a specific index in an ordered list is a natural Two
     * based on its mathematical position relative to an anchor card.
     *
     * @param index   the position to check
     * @param ordered the ordered list of cards
     * @return true if the Two at that position is natural, false otherwise
     */
    public boolean isPositionallyNatural(final int index, final List<Card> ordered) {
        if (index < 0 || index >= ordered.size()) {
            return false;
        }
        final Card c = ordered.get(index);
        if (c.getValue() != CardValue.TWO) {
            return false;
        }

        for (int i = 0; i < ordered.size(); i++) {
            final Card anchor = ordered.get(i);
            if (anchor.getValue().isPotentialWildcard()) {
                continue;
            }
            if (anchor.getValue() == CardValue.ACE) {
                if (ACE_LOW_VALUE  + index - i == TWO_VALUE
                 || ACE_HIGH_VALUE + index - i == TWO_VALUE) {
                    return true;
                }
            } else if (anchor.getValue().getNumericalValue() + index - i == TWO_VALUE) {
                return true;
            }
        }
        return false;
    }
}
