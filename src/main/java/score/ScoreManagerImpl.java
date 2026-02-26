package score;

import java.util.List;

import model.player.Player;
import model.card.*;

/**
 * Implementation of Burraco score calculation logic.
 */
public class ScoreManagerImpl implements ScoreManager{

    private static final int CLOSURE_BONUS = 100;
    private static final int CLEAN_BURRACO_BONUS = 200;
    private static final int DIRTY_BURRACO_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

    @Override
    public int calculateFinalScore(Player player) {

        int totalScore = 0;

        // Closure bonus
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
    private int calculateBurracoBonus(Player player) {

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
     * Checks if a burraco is clean (no Jolly or 2).
     */
    private boolean isCleanBurraco(List<Card> combination) {

        for (Card card : combination) {

            String value = card.getValue();

            if (value.equals("Jolly") || value.equals("2")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates total value of remaining cards in hand.
     */
    private int calculateRemainingHandValue(Player player) {

        int total = 0;

        for (Card card : player.getHand()) {
            total += CardPointCalculator.getCardPoints(card);
        }

        return total;
    }
}
