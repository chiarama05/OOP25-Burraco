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

    private Player player;

    private static final List<Card> CLEAN_BURRACO = List.of(
        new CardImpl("♥", "3"), new CardImpl("♥", "4"), new CardImpl("♥", "5"),
        new CardImpl("♥", "6"), new CardImpl("♥", "7"), new CardImpl("♥", "8"),
        new CardImpl("♥", "9")
    );

    private static final List<Card> DIRTY_BURRACO = List.of(
        new CardImpl("♥", "3"), new CardImpl("♥", "4"), new CardImpl("♥", "5"),
        new CardImpl("♥", "6"), new CardImpl("♥", "7"), new CardImpl("♥", "8"),
        new CardImpl("♕", "Jolly")
    );

    @BeforeEach
    void setUp() {
        this.player = new PlayerImpl("TestPlayer");
    }

    @Test
    void testEvaluateOkWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl("♠", "5"));
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
        this.player.addCardHand(new CardImpl("♠", "K"));
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
        this.player.addCardHand(new CardImpl("♠", "A"));
        assertTrue(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalseMoreThanOneCard() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl("♠", "A"));
        this.player.addCardHand(new CardImpl("♠", "K"));
        assertFalse(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalsePotNotTaken() {
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl("♠", "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalseNoBurraco() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(this.player));
    }

    @Test
    void testPutComboNeverBlockedIfPotNotTaken() {
        this.player.addCardHand(new CardImpl("♠", "5"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, this.player.getHand(), 1));
    }

    @Test
    void testPutComboBlockedWhenHandGoesToZeroWithPot() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, this.player.getHand(), 1));
    }

    @Test
    void testPutComboAllowedWhenOneCardRemainsAndBurracoPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, List.of(this.player.getHand().get(0)), 3));
    }

    @Test
    void testPutComboBlockedWhenOneCardRemainsNoBurracoComboNotBurraco() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, List.of(this.player.getHand().get(0)), 3));
    }

    @Test
    void testPutComboAllowedWhenNewComboIsBurracoAndOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, List.of(this.player.getHand().get(0)), 7));
    }

    @Test
    void testPutComboNotBlockedWhenMoreThanOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        this.player.addCardHand(new CardImpl("♠", "7"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(this.player, List.of(this.player.getHand().get(0)), 3));
    }

    @Test
    void testAttachNeverBlockedIfPotNotTaken() {
        this.player.addCardHand(new CardImpl("♠", "5"));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(this.player, this.player.getHand(), 5));
    }

    @Test
    void testAttachBlockedWhenHandGoesToZeroWithPot() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(this.player, this.player.getHand(), 5));
    }

    @Test
    void testAttachAllowedWhenItCompletesBurracoAndOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        this.player.addCardHand(new CardImpl("♠", "7"));
        final List<Card> attach = List.of(this.player.getHand().get(0), this.player.getHand().get(1));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(this.player, attach, 5));
    }

    @Test
    void testAttachBlockedWhenOneCardRemainsNoBurracoAttachDoesNotComplete() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        final List<Card> attach = List.of(this.player.getHand().get(0));
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(this.player, attach, 3));
    }

    @Test
    void testAttachAllowedWhenOneCardRemainsAndBurracoAlreadyPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl("♠", "5"));
        this.player.addCardHand(new CardImpl("♠", "6"));
        final List<Card> attach = List.of(this.player.getHand().get(0));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(this.player, attach, 3));
    }
}
