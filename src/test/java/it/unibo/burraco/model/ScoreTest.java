package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.score.ScoreImpl;

class ScoreTest {
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String JOLLY_SEED = "♕";

    private static final int CLEAN_BONUS  = 200;
    private static final int DIRTY_BONUS  = 100;
    private static final int CLOSURE_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

    private ScoreImpl score;
    private PlayerImpl player;

    @BeforeEach
    void init() {
        this.score  = new ScoreImpl();
        this.player = new PlayerImpl("TestPlayer");
    }

    @Test
    void testConstantGetters() {
        assertEquals(CLEAN_BONUS,    score.getCleanBurracoBonusValue());
        assertEquals(DIRTY_BONUS,    score.getDirtyBurracoBonusValue());
        assertEquals(CLOSURE_BONUS,  score.getClosureBonusValue());
        assertEquals(NO_POT_PENALTY, score.getNoPotPenalty());
    }

    @Test
    void testRemainingHandValueEmptyHand() {
        assertEquals(0, score.calculateRemainingHandValue(player));
    }

    @Test
    void testRemainingHandValueWithCards() {
        player.addCardHand(new CardImpl(HEARTS, "A"));   // 15
        player.addCardHand(new CardImpl(HEARTS, "K"));   // 10
        player.addCardHand(new CardImpl(HEARTS, "3"));   // 5
        assertEquals(30, score.calculateRemainingHandValue(player));
    }

    @Test
    void testCardsOnTableNoCombinations() {
        assertEquals(0, score.calculateOnlyCardsOnTable(player));
    }

    @Test
    void testCardsOnTableWithCombinations() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "A"),
                new CardImpl(HEARTS, "K"),
                new CardImpl(HEARTS, "3")
        ));
        assertEquals(30, score.calculateOnlyCardsOnTable(player));
    }

    @Test
    void testBurracoBonusNoCombinations() {
        assertEquals(0, score.calculateBurracoBonus(player));
    }

    @Test
    void testCombinationUnder7NotBurraco() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7")
        ));
        assertEquals(0, score.calculateBurracoBonus(player));
        assertEquals(0, score.countCleanBurraco(player));
        assertEquals(0, score.countDirtyBurraco(player));
    }

    @Test
    void testCleanBurracoNoTwosNoJolly() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"),
                new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        assertEquals(CLEAN_BONUS, score.calculateBurracoBonus(player));
        assertEquals(1, score.countCleanBurraco(player));
        assertEquals(0, score.countDirtyBurraco(player));
    }

    @Test
    void testCleanBurracoWithTwoInNaturalPosition() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "A"),
                new CardImpl(HEARTS, "2"),
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7")
        ));
        assertEquals(CLEAN_BONUS, score.calculateBurracoBonus(player));
        assertEquals(1, score.countCleanBurraco(player));
    }

    @Test
    void testDirtyBurracoWithJolly() {
        player.addCombination(List.of(
                new CardImpl(JOLLY_SEED, "Jolly"),
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"),
                new CardImpl(HEARTS, "8")
        ));
        assertEquals(DIRTY_BONUS, score.calculateBurracoBonus(player));
        assertEquals(0, score.countCleanBurraco(player));
        assertEquals(1, score.countDirtyBurraco(player));
    }

    @Test
    void testDirtyBurracoWithTwoNotInNaturalPosition() {
        player.addCombination(List.of(
                new CardImpl(SPADES, "2"),
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"),
                new CardImpl(HEARTS, "8")
        ));
        assertEquals(DIRTY_BONUS, score.calculateBurracoBonus(player));
        assertEquals(0, score.countCleanBurraco(player));
        assertEquals(1, score.countDirtyBurraco(player));
    }

    @Test
    void testDirtyBurracoWithMultipleTwos() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "2"),
                new CardImpl(HEARTS, "2"),
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7")
        ));
        assertEquals(DIRTY_BONUS, score.calculateBurracoBonus(player));
        assertEquals(0, score.countCleanBurraco(player));
        assertEquals(1, score.countDirtyBurraco(player));
    }

    @Test
    void testMultipleBurracoMixed() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        player.addCombination(List.of(
                new CardImpl(JOLLY_SEED, "Jolly"), new CardImpl(SPADES, "3"),
                new CardImpl(SPADES, "4"), new CardImpl(SPADES, "5"),
                new CardImpl(SPADES, "6"), new CardImpl(SPADES, "7"),
                new CardImpl(SPADES, "8")
        ));
        assertEquals(CLEAN_BONUS + DIRTY_BONUS, score.calculateBurracoBonus(player));
        assertEquals(1, score.countCleanBurraco(player));
        assertEquals(1, score.countDirtyBurraco(player));
    }

    @Test
    void testFinalScoreNoPotPenaltyOnly() {
        player.addCardHand(new CardImpl(HEARTS, "3")); 
        assertEquals(-105, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreWithPotNoCombinations() {
        player.setInPot(true);
        player.addCardHand(new CardImpl(HEARTS, "3")); 
        assertEquals(-5, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreClosureBonus() {
        player.setInPot(true);
        final int result = score.calculateFinalScore(player);
        assertEquals(CLOSURE_BONUS, result);
    }

    @Test
    void testFinalScoreHandPenalty() {
        player.setInPot(true);
        player.addCardHand(new CardImpl(HEARTS, "A"));
        player.addCardHand(new CardImpl(HEARTS, "K")); 
        assertEquals(-25, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreFullScenario() {
        player.setInPot(true);
        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        assertEquals(345, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreNoPotPenaltyApplied() {
        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        assertEquals(245, score.calculateFinalScore(player));
    }
}
