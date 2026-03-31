package it.unibo.burraco.model.score;

import java.util.List;
import java.util.stream.Collectors;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;

/**
 * Standard implementation of the Burraco scoring system.
 * Handles bonuses for clean/dirty burracos, closures, and penalties for uncollected pots.
 */
public final class ScoreImpl implements Score {

    private static final int CLOSURE_BONUS = 100;
    private static final int CLEAN_BURRACO_BONUS = 200;
    private static final int DIRTY_BURRACO_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;
    private static final int BURRACO_MIN_CARDS = 7;

    private static final String JOLLY = "Jolly";
    private static final String TWO = "2";

    @Override
    public int calculateFinalScore(final Player player) {
        int totalScore = calculateOnlyCardsOnTable(player);

        if (player.hasFinishedCards()) {
            totalScore += CLOSURE_BONUS;
        }

        totalScore += calculateBurracoBonus(player);

        if (!player.isInPot()) {
            totalScore += NO_POT_PENALTY;
        }

        totalScore -= calculateRemainingHandValue(player);

        return totalScore;
    }

    @Override
    public int calculateBurracoBonus(final Player player) {
        int bonus = 0;
        for (final List<Card> combination : player.getCombinations()) {
            if (combination.size() >= BURRACO_MIN_CARDS) {
                if (isCleanBurraco(combination)) {
                    bonus += CLEAN_BURRACO_BONUS;
                } else {
                    bonus += DIRTY_BURRACO_BONUS;
                }
            }
        }
        return bonus;
    }

    /**
     * Determines if a burraco is clean (no wildcards, or 2 in natural position).
     * 
     * @param combination the list of cards forming the burraco.
     * @return true if the burraco is clean, false otherwise.
     */
    private boolean isCleanBurraco(final List<Card> combination) {
        if (combination.stream().anyMatch(c -> JOLLY.equals(c.getValue()))) {
            return false;
        }

        final List<Card> twos = combination.stream()
                .filter(c -> TWO.equals(c.getValue()))
                .collect(Collectors.toList());

        if (twos.isEmpty()) {
            return true;
        }
        if (twos.size() > 1) {
            return false;
        }

        final Card two = twos.get(0);
        return isTwoInNaturalPosition(two, combination);
    }

    /**
     * Verifies if a '2' acts as a natural card in a sequence of the same suit.
     * 
     * @param two the card with value "2".
     * @param combination the full combination to check.
     * @return true if the '2' is in natural position, false otherwise.
     */
    private boolean isTwoInNaturalPosition(final Card two, final List<Card> combination) {
        final String suit = two.getSeed();

        final boolean sameSuit = combination.stream()
                .allMatch(c -> c.getSeed().equals(suit));
        if (!sameSuit) {
            return false;
        }

        final List<Integer> ranks = combination.stream()
                .map(CardPoint::toInt)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 1; i < ranks.size(); i++) {
            if (ranks.get(i) != ranks.get(i - 1) + 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int countCleanBurraco(final Player player) {
        return (int) player.getCombinations().stream()
                .filter(c -> c.size() >= BURRACO_MIN_CARDS && isCleanBurraco(c)).count();
    }

    @Override
    public int countDirtyBurraco(final Player player) {
        return (int) player.getCombinations().stream()
                .filter(c -> c.size() >= BURRACO_MIN_CARDS && !isCleanBurraco(c)).count();
    }

    @Override
    public int calculateRemainingHandValue(final Player player) {
        int total = 0;
        for (final Card card : player.getHand()) {
            total += CardPoint.getCardPoints(card);
        }
        return total;
    }

    @Override
    public int calculateOnlyCardsOnTable(final Player player) {
        int total = 0;
        for (final List<Card> combination : player.getCombinations()) {
            for (final Card card : combination) {
                total += CardPoint.getCardPoints(card);
            }
        }
        return total;
    }

    @Override
    public int getCleanBurracoBonusValue() { 
        return CLEAN_BURRACO_BONUS; 
    }

    @Override
    public int getDirtyBurracoBonusValue() { 
        return DIRTY_BURRACO_BONUS; 
    }

    @Override
    public int getClosureBonusValue() { 
        return CLOSURE_BONUS; 
    }

    @Override
    public int getNoPotPenalty() { 
        return NO_POT_PENALTY; 
    }
}
