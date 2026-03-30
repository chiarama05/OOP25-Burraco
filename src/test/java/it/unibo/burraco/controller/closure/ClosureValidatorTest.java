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


/**
 * Tests for {@link ClosureValidator}.
 * Covers: evaluate(), evaluateAfterDiscard(), canCloseByDiscarding(),
 * wouldGetStuckAfterPutCombo(), wouldGetStuckAfterAttach().
 */
class ClosureValidatorTest {

   private Player player;

    /** Clean burraco: 7 cards, same suit, sequential, no wildcards. */
    private static final List<Card> CLEAN_BURRACO = List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4"),new CardImpl("♥", "5"), new CardImpl("♥", "6"),new CardImpl("♥", "7"), new CardImpl("♥", "8"),new CardImpl("♥", "9"));

    /** Dirty burraco: 7 cards, contains a Jolly. */
    private static final List<Card> DIRTY_BURRACO = List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4"),new CardImpl("♥", "5"), new CardImpl("♥", "6"),new CardImpl("♥", "7"), new CardImpl("♥", "8"),new CardImpl("♕", "Jolly"));


    @BeforeEach
    void setUp() {
        player = new PlayerImpl("TestPlayer");
    }

    // evaluate()

    //If the hand is not empty the game is still in progress → OK.
    @Test
    void testEvaluateOkWhenHandNotEmpty() {
        player.addCardHand(new CardImpl("♠", "5"));
        assertEquals(ClosureState.OK, ClosureValidator.evaluate(player));
    }

    // Hand empty but pot not yet taken → player must draw pot before anything else.
    @Test
    void testEvaluateZeroCardsNoPotWhenHandEmptyAndPotNotTaken() {
        assertEquals(ClosureState.ZERO_CARDS_NO_POT, ClosureValidator.evaluate(player));
    }


    //Hand empty, pot taken, but no burraco yet → player is stuck and must form one.
    @Test
    void testEvaluateZeroCardsNoBurracoWhenPotTakenNoBurraco() {
        player.setInPot(true);
        assertEquals(ClosureState.ZERO_CARDS_NO_BURRACO, ClosureValidator.evaluate(player));
    }



    //Hand empty, pot taken, at least one burraco → player may close.
    @Test
    void testEvaluateCanCloseWhenPotTakenAndBurracoPresent() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(player));
    }

    //A dirty burraco (with Jolly) still satisfies the closure condition.
    @Test
    void testEvaluateCanCloseWithDirtyBurraco() {
        player.setInPot(true);
        player.addCombination(DIRTY_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(player));
    }



    //evaluateAfterDiscard()

    //After discarding, if hand still has cards → OK.
    @Test
    void testEvaluateAfterDiscardOkWhenHandNotEmpty() {
        player.addCardHand(new CardImpl("♠", "K"));
        player.setInPot(true);
        assertEquals(ClosureState.OK, ClosureValidator.evaluateAfterDiscard(player));
    }

    //Hand empty, pot taken, burraco present → round is won.
    @Test
    void testEvaluateAfterDiscardRoundWonWithPotAndBurraco() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.ROUND_WON, ClosureValidator.evaluateAfterDiscard(player));
    }

    //Hand empty, pot taken, no burraco → cannot close.
    @Test
    void testEvaluateAfterDiscardCannotCloseNoBurraco() {
        player.setInPot(true);
        assertEquals(ClosureState.CANNOT_CLOSE_NO_BURRACO,ClosureValidator.evaluateAfterDiscard(player));
    }

    /*Hand empty but pot not taken → evaluateAfterDiscard returns OK
    (this is not the closing-by-discard scenario).*/
    @Test
    void testEvaluateAfterDiscardOkWhenPotNotTaken() {
        assertEquals(ClosureState.OK, ClosureValidator.evaluateAfterDiscard(player));
    }




    //canCloseByDiscarding()

    //All three conditions met: pot taken, burraco, exactly 1 card in hand.
    @Test
    void testCanCloseByDiscardingTrueAllConditionsMet() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "A"));
        assertTrue(ClosureValidator.canCloseByDiscarding(player));
    }

    //More than one card left → cannot close by discarding.
    @Test
    void testCanCloseByDiscardingFalseMoreThanOneCard() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "A"));
        player.addCardHand(new CardImpl("♠", "K"));
        assertFalse(ClosureValidator.canCloseByDiscarding(player));
    }

    /**
     * Pot not taken → cannot close even with burraco and 1 card.
     */
    @Test
    void testCanCloseByDiscardingFalsePotNotTaken() {
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(player));
    }

    /**
     * No burraco → cannot close even with pot and 1 card.
     */
    @Test
    void testCanCloseByDiscardingFalseNoBurraco() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(player));
    }

    // ── wouldGetStuckAfterPutCombo() ─────────────────────────────────────────

    /**
     * Without pot taken the move is always allowed, even going to 0 cards.
     */
    @Test
    void testPutComboNeverBlockedIfPotNotTaken() {
        player.addCardHand(new CardImpl("♠", "5"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, player.getHand(), 1));
    }

    /**
     * Pot taken, playing all cards → hand goes to 0 → blocked.
     */
    @Test
    void testPutComboBlockedWhenHandGoesToZeroWithPot() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(player, player.getHand(), 1));
    }

    /**
     * Pot taken, 1 card remains after play, burraco already present → allowed
     * (the remaining card will be the winning discard).
     */
    @Test
    void testPutComboAllowedWhenOneCardRemainsAndBurracoPresent() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, List.of(player.getHand().get(0)), 3));
    }

    /**
     * Pot taken, 1 card remains after play, no burraco and combo < 7 → blocked.
     */
    @Test
    void testPutComboBlockedWhenOneCardRemainsNoBurracoComboNotBurraco() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, List.of(player.getHand().get(0)), 3));
    }

    /**
     * The new combination itself is a burraco (size >= 7) and 1 card remains → allowed.
     */
    @Test
    void testPutComboAllowedWhenNewComboIsBurracoAndOneCardRemains() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        // comboSize = 7 → newIsBurraco = true → allowed
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, List.of(player.getHand().get(0)), 7));
    }

    /**
     * Pot taken but more than 1 card remains after play → never blocked.
     */
    @Test
    void testPutComboNotBlockedWhenMoreThanOneCardRemains() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        player.addCardHand(new CardImpl("♠", "7"));
        // plays 1 → 2 remain → fine
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, List.of(player.getHand().get(0)), 3));
    }

    // ── wouldGetStuckAfterAttach() ────────────────────────────────────────────

    /**
     * Without pot taken the attach is always allowed.
     */
    @Test
    void testAttachNeverBlockedIfPotNotTaken() {
        player.addCardHand(new CardImpl("♠", "5"));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(
                player, player.getHand(), 5));
    }

    /**
     * Pot taken, attaching all cards → hand goes to 0 → blocked.
     */
    @Test
    void testAttachBlockedWhenHandGoesToZeroWithPot() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(
                player, player.getHand(), 5));
    }

    /**
     * Pot taken, attach completes a burraco (currentCombo + attach >= 7),
     * 1 card remains → allowed.
     */
    @Test
    void testAttachAllowedWhenItCompletesBurracoAndOneCardRemains() {
        player.setInPot(true);
        // combo currently has 5 cards; attaching 2 → 7 = burraco
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        player.addCardHand(new CardImpl("♠", "7")); // 3 in hand
        final List<Card> attach = List.of(
                player.getHand().get(0), player.getHand().get(1)); // attach 2 → 1 remains
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(player, attach, 5));
    }

    /**
     * Pot taken, 1 card remains after attach, no burraco and attach doesn't
     * complete one → blocked.
     */
    @Test
    void testAttachBlockedWhenOneCardRemainsNoBurracoAttachDoesNotComplete() {
        player.setInPot(true);
        // combo currently has 3 cards; attaching 1 → 4 (not a burraco)
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6")); // 2 in hand
        final List<Card> attach = List.of(player.getHand().get(0)); // attach 1 → 1 remains
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(player, attach, 3));
    }

    /**
     * Pot taken, burraco already present, 1 card remains after attach → allowed.
     */
    @Test
    void testAttachAllowedWhenOneCardRemainsAndBurracoAlreadyPresent() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6")); // 2 in hand
        final List<Card> attach = List.of(player.getHand().get(0)); // 1 remains
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(player, attach, 3));
    }
}
