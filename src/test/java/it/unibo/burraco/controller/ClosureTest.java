package it.unibo.burraco.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

import java.util.List;

/*Closure Management: ClosureValidator (evaluate, evaluateAfterDiscard,
  canCloseByDiscarding, wouldGetStuck*).*/
public class ClosureTest {

    private Player player;

    private static final List<Card> CLEAN_BURRACO = List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4"),new CardImpl("♥", "5"), new CardImpl("♥", "6"),new CardImpl("♥", "7"), new CardImpl("♥", "8"),new CardImpl("♥", "9"));

    private static final List<Card> DIRTY_BURRACO = List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4"),new CardImpl("♥", "5"), new CardImpl("♥", "6"),new CardImpl("♥", "7"), new CardImpl("♥", "8"),new CardImpl("♕", "Jolly"));

    @BeforeEach
    void setUp() {
        player = new PlayerImpl("Tester");
    }

    //evaluate() method
    @Test
    void testEvaluateOkWhenHandNotEmpty() {
        player.addCardHand(new CardImpl("♠", "5"));
        assertEquals(ClosureState.OK, ClosureValidator.evaluate(player));
    }

    @Test
    void testEvaluateZeroCardsNoPotWhenHandEmptyNoPot() {
        assertEquals(ClosureState.ZERO_CARDS_NO_POT, ClosureValidator.evaluate(player));
    }

    @Test
    void testEvaluateZeroCardsNoBurracoWhenPotTakenNoBurraco() {
        player.setInPot(true);
        assertEquals(ClosureState.ZERO_CARDS_NO_BURRACO, ClosureValidator.evaluate(player));
    }

    @Test
    void testEvaluateCanCloseWhenPotAndBurraco() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(player));
    }

    @Test
    void testDirtyBurracoAlsoAllowsCanClose() {
        player.setInPot(true);
        player.addCombination(DIRTY_BURRACO);
        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(player));
    }

    //evaluateAfterDiscard() method

    @Test
    void testEvaluateAfterDiscardOkWhenHandNotEmpty() {
        player.addCardHand(new CardImpl("♠", "K"));
        player.setInPot(true);
        assertEquals(ClosureState.OK, ClosureValidator.evaluateAfterDiscard(player));
    }

    @Test
    void testEvaluateAfterDiscardRoundWonWithBurraco() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        assertEquals(ClosureState.ROUND_WON, ClosureValidator.evaluateAfterDiscard(player));
    }

    @Test
    void testEvaluateAfterDiscardCannotCloseNoBurraco() {
        player.setInPot(true);
        assertEquals(ClosureState.CANNOT_CLOSE_NO_BURRACO,ClosureValidator.evaluateAfterDiscard(player));
    }

    @Test
    void testEvaluateAfterDiscardOkWhenNoPotTaken() {
        // hand empty, pot not taken → OK (not the discard-close scenario)
        assertEquals(ClosureState.OK, ClosureValidator.evaluateAfterDiscard(player));
    }

    //canCloseByDiscarding() method

    @Test
    void testCanCloseByDiscardingTrue() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "A")); // 1 card remain
        assertTrue(ClosureValidator.canCloseByDiscarding(player));
    }

    @Test
    void testCanCloseByDiscardingFalseMoreThanOneCard() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "A"));
        player.addCardHand(new CardImpl("♠", "K"));
        assertFalse(ClosureValidator.canCloseByDiscarding(player));
    }

    @Test
    void testCanCloseByDiscardingFalseNoPot() {
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(player));
    }

    @Test
    void testCanCloseByDiscardingFalseNoBurraco() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "A"));
        assertFalse(ClosureValidator.canCloseByDiscarding(player));
    }

    //wouldGetStuckAfterPutCombo() method

    @Test
    void testPutComboNeverBlockedWithoutPot() {
        player.addCardHand(new CardImpl("♠", "5"));
        List<Card> play = player.getHand();
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(player, play, 1));
    }

    @Test
    void testPutComboBlockedGoToZeroWithPot() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, player.getHand(), 1));
    }

    @Test
    void testPutComboAllowedOneCardRemainsWithBurraco() {
        player.setInPot(true);
        player.addCombination(CLEAN_BURRACO);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        // gioca 1 carta → rimane 1 con burraco già fatto
        assertFalse(ClosureValidator.wouldGetStuckAfterPutCombo(
                player, List.of(player.getHand().get(0)), 3));
    }

    @Test
    void testPutComboBlockedOneCardRemainsNoBurraco() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(player, List.of(player.getHand().get(0)), 3)) ;
        // comboSize < 7 and no burraco → stuck
    }

    //wouldGetStuckAfterAttach() method

    @Test
    void testAttachNeverBlockedWithoutPot() {
        player.addCardHand(new CardImpl("♠", "5"));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(player, player.getHand(), 5));
    }

    @Test
    void testAttachBlockedGoToZeroWithPot() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♠", "5"));
        assertTrue(ClosureValidator.wouldGetStuckAfterAttach(player, player.getHand(), 5));
    }

    @Test
    void testAttachAllowedWhenCompletesBurraco() {
        player.setInPot(true);
        // actual combination = 5 cards, + 2 → 7 → burraco
        player.addCardHand(new CardImpl("♠", "5"));
        player.addCardHand(new CardImpl("♠", "6"));
        player.addCardHand(new CardImpl("♠", "7"));
        List<Card> attach = List.of(player.getHand().get(0), player.getHand().get(1));
        assertFalse(ClosureValidator.wouldGetStuckAfterAttach(player, attach, 5));
    }
}
