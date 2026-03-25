package it.unibo.burraco.model.score;

import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.*;

/**
 * Implementation of Burraco score calculation logic.
 */
public class ScoreImpl implements Score{

    private static final int CLOSURE_BONUS = 100;
    private static final int CLEAN_BURRACO_BONUS = 200;
    private static final int DIRTY_BURRACO_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

    @Override
    public int calculateFinalScore(Player player) {

        int totalScore = calculateOnlyCardsOnTable(player);

    if (player.hasFinishedCards()) {
        totalScore += CLOSURE_BONUS;
    }

        // Burraco bonuses
        totalScore += calculateBurracoBonus(player);

        // Penalty if pot not taken
        if (!player.isInPot()) {
            totalScore += NO_POT_PENALTY;
        }

        // Negative value of remaining hand
        totalScore -= calculateRemainingHandValue(player);

        return totalScore;
    }

    /**
     * Calculates burraco bonuses (clean or dirty).
     */
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

    private boolean isCleanBurraco(List<Card> combination) {


    if (combination.stream().anyMatch(c -> c.getValue().equals("Jolly"))) {
        return false;
    }

    List<Card> twos = combination.stream()
            .filter(c -> c.getValue().equals("2"))
            .collect(Collectors.toList());

    if (twos.isEmpty()) return true;

    if (twos.size() > 1) return false;

    Card two = twos.get(0);
    return isTwoInNaturalPosition(two, combination);
}


private boolean isTwoInNaturalPosition(Card two, List<Card> combination) {

    String suit = two.getSeed();

    boolean sameSuit = combination.stream()
            .allMatch(c -> c.getSeed().equals(suit));

    if (!sameSuit) return false;

    List<Integer> ranks = combination.stream()
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

    public int countCleanBurraco(Player player) {
    return (int) player.getCombinations().stream()
            .filter(c -> c.size() >= 7 && isCleanBurraco(c)).count();
    }

    public int countDirtyBurraco(Player player) {
    return (int) player.getCombinations().stream()
            .filter(c -> c.size() >= 7 && !isCleanBurraco(c)).count();
    }
    

    /**
     * Calculates total value of remaining cards in hand.
     */
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
