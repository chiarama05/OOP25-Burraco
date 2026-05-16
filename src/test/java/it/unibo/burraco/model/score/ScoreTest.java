package it.unibo.burraco.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;
import it.unibo.burraco.model.player.PlayerImpl;

class ScoreTest {

    private static final int CLEAN_BONUS = 200;
    private static final int DIRTY_BONUS = 100;
    private static final int CLOSURE_BONUS = 100;
    private static final int NO_POT_PENALTY = -100;

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
        this.player.addCardHand(new CardImpl(Seed.HEARTS, CardValue.ACE));
        this.player.addCardHand(new CardImpl(Seed.HEARTS, CardValue.KING));
        this.player.addCardHand(new CardImpl(Seed.HEARTS, CardValue.THREE));
        assertEquals(HAND_VALUE_THREE_CARDS, this.score.calculateRemainingHandValue(this.player));
    }

    @Test
    void testCardsOnTableWithCombinations() {
        this.player.addCombination(List.of(
                new CardImpl(Seed.HEARTS, CardValue.ACE),
                new CardImpl(Seed.HEARTS, CardValue.KING),
                new CardImpl(Seed.HEARTS, CardValue.THREE)
        ));
        assertEquals(HAND_VALUE_THREE_CARDS, this.score.calculateOnlyCardsOnTable(this.player));
    }

    @Test
    void testCleanBurracoNoTwosNoJolly() {
        this.player.addCombination(List.of(
                new CardImpl(Seed.HEARTS, CardValue.THREE), new CardImpl(Seed.HEARTS, CardValue.FOUR),
                new CardImpl(Seed.HEARTS, CardValue.FIVE), new CardImpl(Seed.HEARTS, CardValue.SIX),
                new CardImpl(Seed.HEARTS, CardValue.SEVEN), new CardImpl(Seed.HEARTS, CardValue.EIGHT),
                new CardImpl(Seed.HEARTS, CardValue.NINE)
        ));
        assertEquals(CLEAN_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countCleanBurraco(this.player));
        assertEquals(0, this.score.countDirtyBurraco(this.player));
    }

    @Test
    void testCleanBurracoWithTwoInNaturalPosition() {
        this.player.addCombination(List.of(
                new CardImpl(Seed.HEARTS, CardValue.ACE), new CardImpl(Seed.HEARTS, CardValue.TWO),
                new CardImpl(Seed.HEARTS, CardValue.THREE), new CardImpl(Seed.HEARTS, CardValue.FOUR),
                new CardImpl(Seed.HEARTS, CardValue.FIVE), new CardImpl(Seed.HEARTS, CardValue.SIX),
                new CardImpl(Seed.HEARTS, CardValue.SEVEN)
        ));
        assertEquals(CLEAN_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countCleanBurraco(this.player));
    }

    @Test
    void testDirtyBurracoWithJolly() {
        this.player.addCombination(List.of(
                new CardImpl(Seed.JOKER, CardValue.JOLLY),
                new CardImpl(Seed.HEARTS, CardValue.THREE), new CardImpl(Seed.HEARTS, CardValue.FOUR),
                new CardImpl(Seed.HEARTS, CardValue.FIVE), new CardImpl(Seed.HEARTS, CardValue.SIX),
                new CardImpl(Seed.HEARTS, CardValue.SEVEN), new CardImpl(Seed.HEARTS, CardValue.EIGHT)
        ));
        assertEquals(DIRTY_BONUS, this.score.calculateBurracoBonus(this.player));
        assertEquals(1, this.score.countDirtyBurraco(this.player));
    }

    @Test
    void testFinalScoreNoPotPenaltyApplied() {
        this.player.addCombination(List.of(
                new CardImpl(Seed.HEARTS, CardValue.THREE), new CardImpl(Seed.HEARTS, CardValue.FOUR),
                new CardImpl(Seed.HEARTS, CardValue.FIVE), new CardImpl(Seed.HEARTS, CardValue.SIX),
                new CardImpl(Seed.HEARTS, CardValue.SEVEN), new CardImpl(Seed.HEARTS, CardValue.EIGHT),
                new CardImpl(Seed.HEARTS, CardValue.NINE)
        ));
        assertEquals(SCORE_NO_POT, this.score.calculateFinalScore(this.player));
    }

    @Test
    void testFinalScoreFullScenario() {
        this.player.setInPot(true);
        this.player.addCombination(List.of(
                new CardImpl(Seed.HEARTS, CardValue.THREE), new CardImpl(Seed.HEARTS, CardValue.FOUR),
                new CardImpl(Seed.HEARTS, CardValue.FIVE), new CardImpl(Seed.HEARTS, CardValue.SIX),
                new CardImpl(Seed.HEARTS, CardValue.SEVEN), new CardImpl(Seed.HEARTS, CardValue.EIGHT),
                new CardImpl(Seed.HEARTS, CardValue.NINE)
        ));
        assertEquals(SCORE_FULL, this.score.calculateFinalScore(this.player));
    }
}