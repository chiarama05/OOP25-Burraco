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
    private static final String SIX = "6";
    private static final String SEVEN = "7";
    private static final String ACE = "A";
    private static final String KING = "K";
    private static final String PLAYER_NAME = "TestPlayer";

    private static final int BURRACO_SIZE = 7;
    private static final int SMALL_COMBO = 3;
    private static final int ATTACH_SIZE = 5;

    private static final List<Card> CLEAN_BURRACO = List.of(
        new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"), new CardImpl(HEARTS, FIVE),
        new CardImpl(HEARTS, SIX), new CardImpl(HEARTS, SEVEN), new CardImpl(HEARTS, "8"),
        new CardImpl(HEARTS, "9")
    );

    private static final List<Card> DIRTY_BURRACO = List.of(
        new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"), new CardImpl(HEARTS, FIVE),
        new CardImpl(HEARTS, SIX), new CardImpl(HEARTS, SEVEN), new CardImpl(HEARTS, "8"),
        new CardImpl(JOLLY_SEED, JOLLY_VAL)
    );

    private Player player;
    private ClosureValidator validator;

    @BeforeEach
    void setUp() {
        this.player = new PlayerImpl(PLAYER_NAME);
        this.validator = new ClosureValidator();
    }

    @Test
    void testEvaluateOkWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertEquals(ClosureState.OK, validator.evaluate(this.player));
    }

    @Test
    void testEvaluateZeroCardsNoPotWhenHandEmptyAndPotNotTaken() {
        assertEquals(ClosureState.ZERO_CARDS_NO_POT, validator.evaluate(this.player));
    }

    @Test
    void testEvaluateZeroCardsNoBurracoWhenPotTakenNoBurraco() {
        this.player.setInPot(true);
        assertEquals(ClosureState.ZERO_CARDS_NO_BURRACO, validator.evaluate(this.player));
    }

    @Test
    void testEvaluateCanCloseWhenPotTakenAndBurracoPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, validator.evaluate(this.player));
    }

    @Test
    void testEvaluateCanCloseWithDirtyBurraco() {
        this.player.setInPot(true);
        this.player.addCombination(DIRTY_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, validator.evaluate(this.player));
    }

    @Test
    void testEvaluateAfterDiscardOkWhenHandNotEmpty() {
        this.player.addCardHand(new CardImpl(SPADES, KING));
        this.player.setInPot(true);
        assertEquals(ClosureState.OK, validator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testEvaluateAfterDiscardRoundWonWithPotAndBurraco() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.ROUND_WON, validator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testEvaluateAfterDiscardCannotCloseNoBurraco() {
        this.player.setInPot(true);
        assertEquals(ClosureState.CANNOT_CLOSE_NO_BURRACO, validator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testEvaluateAfterDiscardOkWhenPotNotTaken() {
        assertEquals(ClosureState.OK, validator.evaluateAfterDiscard(this.player));
    }

    @Test
    void testCanCloseByDiscardingTrueAllConditionsMet() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, ACE));
        assertTrue(validator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalseMoreThanOneCard() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, ACE));
        this.player.addCardHand(new CardImpl(SPADES, KING));
        assertFalse(validator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalsePotNotTaken() {
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, ACE));
        assertFalse(validator.canCloseByDiscarding(this.player));
    }

    @Test
    void testCanCloseByDiscardingFalseNoBurraco() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, ACE));
        assertFalse(validator.canCloseByDiscarding(this.player));
    }

    @Test
    void testPutComboNeverBlockedIfPotNotTaken() {
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertFalse(validator.wouldGetStuckAfterPutCombo(this.player, this.player.getHand(), 1));
    }

    @Test
    void testPutComboBlockedWhenHandGoesToZeroWithPot() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertTrue(validator.wouldGetStuckAfterPutCombo(this.player, this.player.getHand(), 1));
    }

    @Test
    void testPutComboAllowedWhenOneCardRemainsAndBurracoPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        assertFalse(validator.wouldGetStuckAfterPutCombo(this.player,
            List.of(this.player.getHand().get(0)), SMALL_COMBO));
    }

    @Test
    void testPutComboBlockedWhenOneCardRemainsNoBurracoComboNotBurraco() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        assertTrue(validator.wouldGetStuckAfterPutCombo(this.player,
            List.of(this.player.getHand().get(0)), SMALL_COMBO));
    }

    @Test
    void testPutComboAllowedWhenNewComboIsBurracoAndOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        assertFalse(validator.wouldGetStuckAfterPutCombo(this.player,
            List.of(this.player.getHand().get(0)), BURRACO_SIZE));
    }

    @Test
    void testPutComboNotBlockedWhenMoreThanOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        this.player.addCardHand(new CardImpl(SPADES, SEVEN));
        assertFalse(validator.wouldGetStuckAfterPutCombo(this.player,
            List.of(this.player.getHand().get(0)), SMALL_COMBO));
    }

    @Test
    void testAttachNeverBlockedIfPotNotTaken() {
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertFalse(validator.wouldGetStuckAfterAttach(this.player,
            this.player.getHand(), ATTACH_SIZE));
    }

    @Test
    void testAttachBlockedWhenHandGoesToZeroWithPot() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        assertTrue(validator.wouldGetStuckAfterAttach(this.player,
            this.player.getHand(), ATTACH_SIZE));
    }

    @Test
    void testAttachAllowedWhenItCompletesBurracoAndOneCardRemains() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        this.player.addCardHand(new CardImpl(SPADES, SEVEN));
        final List<Card> attach = List.of(this.player.getHand().get(0), this.player.getHand().get(1));
        assertFalse(validator.wouldGetStuckAfterAttach(this.player, attach, ATTACH_SIZE));
    }

    @Test
    void testAttachBlockedWhenOneCardRemainsNoBurracoAttachDoesNotComplete() {
        this.player.setInPot(true);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        final List<Card> attach = List.of(this.player.getHand().get(0));
        assertTrue(validator.wouldGetStuckAfterAttach(this.player, attach, SMALL_COMBO));
    }

    @Test
    void testAttachAllowedWhenOneCardRemainsAndBurracoAlreadyPresent() {
        this.player.setInPot(true);
        this.player.addCombination(CLEAN_BURRACO);
        this.player.addCardHand(new CardImpl(SPADES, FIVE));
        this.player.addCardHand(new CardImpl(SPADES, SIX));
        final List<Card> attach = List.of(this.player.getHand().get(0));
        assertFalse(validator.wouldGetStuckAfterAttach(this.player, attach, SMALL_COMBO));
    }
}
