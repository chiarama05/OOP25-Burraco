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

    private static final int CLEAN_BONUS = 200;
    private static final int DIRTY_BONUS = 100;
    private static final int CLOSURE_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

    private ScoreImpl score;
    private PlayerImpl player;

    @BeforeEach
    void init() {
        this.score = new ScoreImpl();
        this.player = new PlayerImpl("TestPlayer");
    }

    @Test
    void testConstantGetters() {
        assertEquals(CLEAN_BONUS, this.score.getCleanBurracoBonusValue());
        assertEquals(DIRTY_BONUS, this.score.getDirtyBurracoBonusValue());
        assertEquals(CLOSURE_BONUS, this.score.getClosureBonusValue());
        assertEquals(NO_POT_PENALTY, this.score.getNoPotPenalty());
    }

    @Test
    void testRemainingHandValueEmptyHand() {
        assertEquals(0, this.score.calculateRemainingHandValue(this.player));
    }

    @Test
    void testRemainingHandValueWithCards() {
        this.player.addCardHand(new CardImpl(HEARTS, "A"));   
        this.player.addCardHand(new CardImpl(HEARTS, "K"));   
        this.player.addCardHand(new CardImpl(HEARTS, "3"));  
        assertEquals(30, this.score.calculateRemainingHandValue(this.player));
    }

    @Test
    void testCardsOnTableWithCombinations() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, "A"),
                new CardImpl(HEARTS, "K"),
                new CardImpl(HEARTS, "3")
        ));
        assertEquals(30, this.score.calculateOnlyCardsOnTable(this.player));
    }

    @Test
    void testCleanBurracoNoTwosNoJolly() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        assertEquals(CLEAN_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countCleanBurraco(this.player));
        assertEquals(0, this.score.countDirtyBurraco(this.player));
    }

    @Test
    void testCleanBurracoWithTwoInNaturalPosition() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, "A"), new CardImpl(HEARTS, "2"),
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7")
        ));
        assertEquals(CLEAN_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countCleanBurraco(this.player));
    }

    @Test
    void testDirtyBurracoWithJolly() {
        this.player.addCombination(List.of(
                new CardImpl(JOLLY_SEED, "Jolly"),
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8")
        ));
        assertEquals(DIRTY_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countDirtyBurraco(this.player));
    }

    @Test
    void testFinalScoreNoPotPenaltyApplied() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        assertEquals(245, this.score.calculateFinalScore(this.player));
    }

    @Test
    void testFinalScoreFullScenario() {
        this.player.setInPot(true);
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));
        assertEquals(345, this.score.calculateFinalScore(this.player));
    }
}
