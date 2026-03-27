package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.model.score.ScoreImpl;

import java.util.List;

/**
 * Gestione vittoria: calcolo punteggio finale, bonus burraco,
 * penalità pozzetto, punteggio cumulato partita.
 */
public class VictoryTest {

    private Player player;
    private Score score;

    private static final List<Card> CLEAN_BURRACO = List.of(
            new CardImpl("♥", "3"), new CardImpl("♥", "4"),
            new CardImpl("♥", "5"), new CardImpl("♥", "6"),
            new CardImpl("♥", "7"), new CardImpl("♥", "8"),
            new CardImpl("♥", "9"));  // 5*5 + 2*10 = 45 pt

    private static final List<Card> DIRTY_BURRACO = List.of(
            new CardImpl("♥", "3"), new CardImpl("♥", "4"),
            new CardImpl("♥", "5"), new CardImpl("♥", "6"),
            new CardImpl("♥", "7"), new CardImpl("♥", "8"),
            new CardImpl("♕", "Jolly")); // Jolly = 30 pt

    private static final List<Card> SHORT_COMBO = List.of(
            new CardImpl("♠", "5"), new CardImpl("♠", "6"),
            new CardImpl("♠", "7")); // non è burraco

    @BeforeEach
    void setUp() {
        player = new PlayerImpl("Tester");
        score  = new ScoreImpl();
    }

    // ── Valori bonus/penalità ─────────────────────────────────────────────────

    @Test
    void testCleanBurracoBonusIs200() {
        assertEquals(200, score.getCleanBurracoBonusValue());
    }

    @Test
    void testDirtyBurracoBonusIs100() {
        assertEquals(100, score.getDirtyBurracoBonusValue());
    }

    @Test
    void testClosureBonusIs100() {
        assertEquals(100, score.getClosureBonusValue());
    }

    @Test
    void testNoPotPenaltyIsNegative() {
        assertTrue(score.getNoPotPenalty() < 0);
    }

    // ── Conteggio burraco ─────────────────────────────────────────────────────

    @Test
    void testCountCleanBurracoZero() {
        assertEquals(0, score.countCleanBurraco(player));
    }

    @Test
    void testCountCleanBurracoOne() {
        player.addCombination(CLEAN_BURRACO);
        assertEquals(1, score.countCleanBurraco(player));
    }

    @Test
    void testCountDirtyBurracoOne() {
        player.addCombination(DIRTY_BURRACO);
        assertEquals(1, score.countDirtyBurraco(player));
    }

    @Test
    void testShortComboNotBurraco() {
        player.addCombination(SHORT_COMBO);
        assertEquals(0, score.countCleanBurraco(player));
        assertEquals(0, score.countDirtyBurraco(player));
    }

    @Test
    void testMixedCombinationsCounted() {
        player.addCombination(CLEAN_BURRACO);
        player.addCombination(DIRTY_BURRACO);
        player.addCombination(SHORT_COMBO);
        assertEquals(1, score.countCleanBurraco(player));
        assertEquals(1, score.countDirtyBurraco(player));
    }

    // ── calculateBurracoBonus() ───────────────────────────────────────────────

    @Test
    void testBurracoBonusZeroNoCombinations() {
        assertEquals(0, score.calculateBurracoBonus(player));
    }

    @Test
    void testBurracoBonusClean() {
        player.addCombination(CLEAN_BURRACO);
        assertEquals(200, score.calculateBurracoBonus(player));
    }

    @Test
    void testBurracoBonusDirty() {
        player.addCombination(DIRTY_BURRACO);
        assertEquals(100, score.calculateBurracoBonus(player));
    }

    @Test
    void testBurracoBonusBothTypes() {
        player.addCombination(CLEAN_BURRACO);
        player.addCombination(DIRTY_BURRACO);
        assertEquals(300, score.calculateBurracoBonus(player));
    }

    // ── calculateRemainingHandValue() ─────────────────────────────────────────

    @Test
    void testRemainingHandZeroWhenEmpty() {
        assertEquals(0, score.calculateRemainingHandValue(player));
    }

    @Test
    void testRemainingHandWithJollyAndAce() {
        player.addCardHand(new CardImpl("♕", "Jolly")); // 30
        player.addCardHand(new CardImpl("♥", "A"));     // 15
        assertEquals(45, score.calculateRemainingHandValue(player));
    }

    @Test
    void testRemainingHandLowCards() {
        player.addCardHand(new CardImpl("♥", "3")); // 5
        player.addCardHand(new CardImpl("♥", "5")); // 5
        assertEquals(10, score.calculateRemainingHandValue(player));
    }

    // ── calculateFinalScore() ─────────────────────────────────────────────────

    @Test
    void testFinalScoreNoPotPenalty() {
        // Nessun pozzetto, nessun burraco, 1 carta K=10 in mano
        player.addCardHand(new CardImpl("♥", "K"));
        // 0 (tavolo) - 100 (no pot) - 10 (mano) = -110
        assertEquals(-110, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreCleanBurracoWithClosure() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        // hand empty → chiusura +100, burraco clean +200, carte tavolo = 45
        int cardsOnTable = score.calculateOnlyCardsOnTable(player);
        int expected = cardsOnTable + 100 + 200;
        assertEquals(expected, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreDirtyBurracoWithClosure() {
        player.setInPot(true);
        player.addCombination(DIRTY_BURRACO);
        int cardsOnTable = score.calculateOnlyCardsOnTable(player);
        int expected = cardsOnTable + 100 + 100;
        assertEquals(expected, score.calculateFinalScore(player));
    }

    @Test
    void testFinalScoreDeductsHandCards() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♥", "K")); // 10 pt rimasta in mano
        // Nessun bonus chiusura (mano non vuota), burraco +200, tavolo, -10 mano
        int cardsOnTable = score.calculateOnlyCardsOnTable(player);
        int expected = cardsOnTable + 200 - 10;
        assertEquals(expected, score.calculateFinalScore(player));
    }

    // ── Match total score ─────────────────────────────────────────────────────

    @Test
    void testMatchScoreStartsAtZero() {
        assertEquals(0, player.getMatchTotalScore());
    }

    @Test
    void testAddPointsAccumulates() {
        player.addPointsToMatch(300);
        player.addPointsToMatch(150);
        assertEquals(450, player.getMatchTotalScore());
    }

    @Test
    void testAddNegativePoints() {
        player.addPointsToMatch(-110);
        assertEquals(-110, player.getMatchTotalScore());
    }

    @Test
    void testResetForNewRoundDoesNotClearMatchScore() {
        player.addPointsToMatch(500);
        player.resetForNewRound();
        assertEquals(500, player.getMatchTotalScore());
    }
}
