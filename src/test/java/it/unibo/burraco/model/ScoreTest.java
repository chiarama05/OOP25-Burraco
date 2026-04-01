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
    private static final String JOLLY_SEED = "♕";

    private static final int CLEAN_BONUS = 200;
    private static final int DIRTY_BONUS = 100;
    private static final int CLOSURE_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

    private static final String V3 = "3";
    private static final String V4 = "4";
    private static final String V5 = "5";
    private static final String V6 = "6";
    private static final String V7 = "7";
    private static final String V8 = "8";
    private static final String V9 = "9";
    private static final String VA = "A";
    private static final String V2 = "2";
    private static final String VK = "K";

    private static final int HAND_VALUE_THREE_CARDS = 30;
    private static final int SCORE_NO_POT = 245;
    private static final int SCORE_FULL = 345;

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
        this.player.addCardHand(new CardImpl(HEARTS, VA));
        this.player.addCardHand(new CardImpl(HEARTS, VK));
        this.player.addCardHand(new CardImpl(HEARTS, V3));
        assertEquals(HAND_VALUE_THREE_CARDS, this.score.calculateRemainingHandValue(this.player));
    }

    @Test
    void testCardsOnTableWithCombinations() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, VA),
                new CardImpl(HEARTS, VK),
                new CardImpl(HEARTS, V3)
        ));
        assertEquals(HAND_VALUE_THREE_CARDS, this.score.calculateOnlyCardsOnTable(this.player));
    }

    @Test
    void testCleanBurracoNoTwosNoJolly() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, V3), new CardImpl(HEARTS, V4),
                new CardImpl(HEARTS, V5), new CardImpl(HEARTS, V6),
                new CardImpl(HEARTS, V7), new CardImpl(HEARTS, V8),
                new CardImpl(HEARTS, V9)
        ));
        assertEquals(CLEAN_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countCleanBurraco(this.player));
        assertEquals(0, this.score.countDirtyBurraco(this.player));
    }

    @Test
    void testCleanBurracoWithTwoInNaturalPosition() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, VA), new CardImpl(HEARTS, V2),
                new CardImpl(HEARTS, V3), new CardImpl(HEARTS, V4),
                new CardImpl(HEARTS, V5), new CardImpl(HEARTS, V6),
                new CardImpl(HEARTS, V7)
        ));
        assertEquals(CLEAN_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countCleanBurraco(this.player));
    }

    @Test
    void testDirtyBurracoWithJolly() {
        this.player.addCombination(List.of(
                new CardImpl(JOLLY_SEED, "Jolly"),
                new CardImpl(HEARTS, V3), new CardImpl(HEARTS, V4),
                new CardImpl(HEARTS, V5), new CardImpl(HEARTS, V6),
                new CardImpl(HEARTS, V7), new CardImpl(HEARTS, V8)
        ));
        assertEquals(DIRTY_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countDirtyBurraco(this.player));
    }

    @Test
    void testFinalScoreNoPotPenaltyApplied() {
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, V3), new CardImpl(HEARTS, V4),
                new CardImpl(HEARTS, V5), new CardImpl(HEARTS, V6),
                new CardImpl(HEARTS, V7), new CardImpl(HEARTS, V8),
                new CardImpl(HEARTS, V9)
        ));
        assertEquals(SCORE_NO_POT, this.score.calculateFinalScore(this.player));
    }

    @Test
    void testFinalScoreFullScenario() {
        this.player.setInPot(true);
        this.player.addCombination(List.of(
                new CardImpl(HEARTS, V3), new CardImpl(HEARTS, V4),
                new CardImpl(HEARTS, V5), new CardImpl(HEARTS, V6),
                new CardImpl(HEARTS, V7), new CardImpl(HEARTS, V8),
                new CardImpl(HEARTS, V9)
        ));
        assertEquals(SCORE_FULL, this.score.calculateFinalScore(this.player));
    }
}
