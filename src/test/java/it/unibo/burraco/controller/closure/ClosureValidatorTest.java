package it.unibo.burraco.controller.closure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

class ClosureValidatorTest {

    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String JOLLY_SEED = "♕";
    private static final String JOLLY_VAL = "Jolly";
    private static final String FIVE = "5";
    private static final String PLAYER_NAME = "TestPlayer";
    
    private static final int BURRACO_SIZE = 7;
    private static final int SMALL_COMBO = 3;
    private static final int ATTACH_SIZE = 5;

    private static final List<Card> CLEAN_BURRACO = List.of(
        new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"), new CardImpl(HEARTS, "5"),
        new CardImpl(HEARTS, "6"), new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
        new CardImpl(HEARTS, "9")
    );

    private static final List<Card> DIRTY_BURRACO = List.of(
        new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"), new CardImpl(HEARTS, "5"),
        new CardImpl(HEARTS, "6"), new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
        new CardImpl(JOLLY_SEED, JOLLY_VAL)
    );

    private Player player;

    @BeforeEach
    void setUp() {
        this.player = new PlayerImpl(PLAYER_NAME);
    }

    @Test
    void testEvaluateOkWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertEquals(ClosureState.OK, ClosureValidator.evaluate(this.player));
    }

    @Test
    void testEvaluateZeroCardsNoPotWhenHandEmptyAndPotNotTaken() {
        assertEquals(ClosureState.ZERO_CARDS_NO_POT, ClosureValidator.evaluate(this.player));
    }

    @Test
    void testEvaluateZeroCardsNoBurracoWhenPotTakenNoBurraco() {
        this.player.setInPot(true);
        assertEquals(ClosureState.ZERO_CARDS_NO_BURRACO, ClosureValidator.evaluate(this.player));
    }

    @Test
    void testEvaluateCanCloseWhenPotTakenAndBurracoPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(this.player));
    }

    @Test
    void testEvaluateCanCloseWithDirtyBurraco() {
        this.player.setInPot(true);
        this.player.addCombination(DIRTY_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(this.player));
    }

    @Test
    void testEvaluateAfterDiscardOkWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl(SPADES, "K"));
        this.player.setInPot(true);
        assertEquals(ClosureState.OK, ClosureValidator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testEvaluateAfterDiscardRoundWonWithPotAndBurraco() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.ROUND_WON, ClosureValidator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testEvaluateAfterDiscardCannotCloseNoBurraco() {
        this.player.setInPot(true);
        assertEquals(ClosureState.CANNOT_CLOSE_NO_BURRACO, ClosureValidator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testEvaluateAfterDiscardOkWhenPotNotTaken() {
        assertEquals(ClosureState.OK, ClosureValidator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testCanCloseByDiscardingTrueAllConditionsMet() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, "A"));
        assertTrue(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalseMoreThanOneCard() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, "A"));
        this.player.addCardHand(new CardImpl(SPADES, "K"));
        assertFalse(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalsePotNotTaken() {
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalseNoBurraco() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testPutComboNeverBlockedIfPotNotTaken() {
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, this.player.getHand(), 1));
    }

    @Test
    void testPutComboBlockedWhenHandGoesToZeroWithPot() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, this.player.getHand(), 1));
    }

    @Test
    void testPutComboAllowedWhenOneCardRemainsAndBurracoPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, 
            List.of(this.player.getHand().get(0)), SMALL_COMBO));
    }

    @Test
    void testPutComboBlockedWhenOneCardRemainsNoBurracoComboNotBurraco() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, 
            List.of(this.player.getHand().get(0)), SMALL_COMBO));
    }

    @Test
    void testPutComboAllowedWhenNewComboIsBurracoAndOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, 
            List.of(this.player.getHand().get(0)), BURRACO_SIZE));
    }

    @Test
    void testPutComboNotBlockedWhenMoreThanOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        this.player.addCardHand(new CardImpl(SPADES, "7"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, 
            List.of(this.player.getHand().get(0)), SMALL_COMBO));
    }

    @Test
    void testAttachNeverBlockedIfPotNotTaken() {
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(this.player, 
            this.player.getHand(), ATTACH_SIZE));
    }

    @Test
    void testAttachBlockedWhenHandGoesToZeroWithPot() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(this.player, 
            this.player.getHand(), ATTACH_SIZE));
    }

    @Test
    void testAttachAllowedWhenItCompletesBurracoAndOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        this.player.addCardHand(new CardImpl(SPADES, "7"));
        final List<Card> attach = List.of(this.player.getHand().get(0), this.player.getHand().get(1));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(this.player, attach, ATTACH_SIZE));
    }

    @Test
    void testAttachBlockedWhenOneCardRemainsNoBurracoAttachDoesNotComplete() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        final List<Card> attach = List.of(this.player.getHand().get(0));
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(this.player, attach, SMALL_COMBO));
    }

    @Test
    void testAttachAllowedWhenOneCardRemainsAndBurracoAlreadyPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, "6"));
        final List<Card> attach = List.of(this.player.getHand().get(0));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(this.player, attach, SMALL_COMBO));
    }
}
