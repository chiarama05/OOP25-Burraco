package it.unibo.burraco.model.score;

import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.*;

/**
 * Standard implementation of the Burraco scoring system.
 * Handles bonuses for clean/dirty burracos, closures, and penalties for uncollected pots.
 */
public class ScoreImpl implements Score{

    private static final int CLOSURE_BONUS = 100;
    private static final int CLEAN_BURRACO_BONUS = 200;
    private static final int DIRTY_BURRACO_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

    @Override
    public int calculateFinalScore(Player player) {
        int totalScore = calculateOnlyCardsOnTable(player);

        // Add closure bonus if player triggered the end of the round
        if (player.hasFinishedCards()) {
            totalScore += CLOSURE_BONUS;
        }

        totalScore += calculateBurracoBonus(player);

        // Apply penalty if the player never collected their "pot"
        if (!player.isInPot()) {
            totalScore += NO_POT_PENALTY;
        }

        // Subtract the value of cards left in hand
        totalScore -= calculateRemainingHandValue(player);

        return totalScore;
    }


    @Override
    public int calculateBurracoBonus(Player player) {
        int bonus = 0;
        for (List<Card> combination : player.getCombinations()) {
            if (combination.size() >= 7) {
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
     */
    private boolean isCleanBurraco(List<Card> combination) {
        // A clean burraco cannot contain a Jolly
        if (combination.stream().anyMatch(c -> c.getValue().equals("Jolly"))) {
            return false;
        }

        List<Card> twos = combination.stream()
                .filter(c -> c.getValue().equals("2"))
                .collect(Collectors.toList());

        // If no '2' is present, it's clean
        if (twos.isEmpty()) return true;
        // More than one '2' in a sequence (unless natural) makes it dirty
        if (twos.size() > 1) return false;

        Card two = twos.get(0);
        // Check if the single '2' is in its natural position within the sequence
        return isTwoInNaturalPosition(two, combination);
    }

    /**
     * Verifies if a '2' acts as a natural card in a sequence of the same suit.
     */
    private boolean isTwoInNaturalPosition(Card two, List<Card> combination) {
        String suit = two.getSeed();

        // Natural positions only exist in sequences of the same suit
        boolean sameSuit = combination.stream()
                .allMatch(c -> c.getSeed().equals(suit));
        if (!sameSuit) return false;

        List<Integer> ranks = combination.stream()
                .map(CardPoint::toInt)   
                .sorted()
                .collect(Collectors.toList());

        // Check if the sequence is mathematically continuous (e.g., 1, 2, 3...)
        for (int i = 1; i < ranks.size(); i++) {
            if (ranks.get(i) != ranks.get(i - 1) + 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int countCleanBurraco(Player player) {
        return (int) player.getCombinations().stream()
                .filter(c -> c.size() >= 7 && isCleanBurraco(c)).count();
    }
    
    @Override
    public int countDirtyBurraco(Player player) {
        return (int) player.getCombinations().stream()
                .filter(c -> c.size() >= 7 && !isCleanBurraco(c)).count();
    }
    
    @Override
    public int calculateRemainingHandValue(Player player) {
        int total = 0;
        for (Card card : player.getHand()) {
            total += CardPoint.getCardPoints(card);
        }
        return total;
    }

    @Override
    public int calculateOnlyCardsOnTable(Player player) {
        int total = 0;
        for (List<Card> combination : player.getCombinations()) {
            for (Card card : combination) {
                total += CardPoint.getCardPoints(card);
            }
        }
        return total;
    }

    @Override public int getCleanBurracoBonusValue() { return CLEAN_BURRACO_BONUS; }
    @Override public int getDirtyBurracoBonusValue() { return DIRTY_BURRACO_BONUS; }
    @Override public int getClosureBonusValue() { return CLOSURE_BONUS; }
    @Override public int getNoPotPenalty() { return NO_POT_PENALTY; }
}