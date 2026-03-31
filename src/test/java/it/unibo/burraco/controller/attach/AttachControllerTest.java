package it.unibo.burraco.controller.attach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;

/**
 * Test class for AttachController.
 */
class AttachControllerTest {
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";
    private static final String SEVEN = "7";
    private static final String EIGHT = "8";
    private static final String NINE = "9";
    private static final String KING = "K";

    private AttachController controller;
    private PlayerImpl player;

    @BeforeEach
    void init() {
        this.controller = new AttachController();
        this.player = new PlayerImpl("TestPlayer");
    }

    private List<Card> setupCombination(final List<Card> cards) {
        player.addCombination(cards);
        return player.getCombinations().get(player.getCombinations().size() - 1);
    }

    @Test
    void testNotDrawn() {
        final List<Card> selected = List.of(new CardImpl(HEARTS, SIX));
        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        )));

        final AttachResult result = controller.tryAttach(player, selected, combo, false, true);
        assertEquals(AttachResult.NOT_DRAWN, result);
    }

    @Test
    void testWrongPlayer() {
        final List<Card> selected = List.of(new CardImpl(HEARTS, SIX));
        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        )));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, false);
        assertEquals(AttachResult.WRONG_PLAYER, result);
    }

    @Test
    void testNoCardsSelected() {
        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        )));

        final AttachResult result = controller.tryAttach(player, new ArrayList<>(), combo, true, true);
        assertEquals(AttachResult.NO_CARDS_SELECTED, result);
    }

    @Test
    void testInvalidCombination() {
        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        )));
        final List<Card> selected = List.of(new CardImpl(SPADES, NINE));
        player.addCardHand(selected.get(0));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.INVALID_COMBINATION, result);
    }

    @Test
    void testWouldGetStuckAfterAttach() {
        player.setInPot(true);

        final CardImpl six = new CardImpl(HEARTS, SIX);
        player.addCardHand(six);

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        )));
        final List<Card> selected = new ArrayList<>(List.of(six));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.WOULD_GET_STUCK, result);
    }

    @Test
    void testSuccess() {
        player.setInPot(true);

        player.addCombination(List.of(
                new CardImpl(HEARTS, THREE), new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE), new CardImpl(HEARTS, SIX),
                new CardImpl(HEARTS, SEVEN), new CardImpl(HEARTS, EIGHT),
                new CardImpl(HEARTS, NINE)
        ));

        final CardImpl sixSpades = new CardImpl(SPADES, SIX);
        final CardImpl sevenSpades = new CardImpl(SPADES, SEVEN);
        player.addCardHand(sixSpades);
        player.addCardHand(sevenSpades);

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(SPADES, THREE),
                new CardImpl(SPADES, FOUR),
                new CardImpl(SPADES, FIVE)
        )));
        final List<Card> selected = new ArrayList<>(List.of(sixSpades));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.SUCCESS, result);
    }

    @Test
    void testSuccessRemovesCardFromHand() {
        player.setInPot(true);

        player.addCombination(List.of(
                new CardImpl(HEARTS, THREE), new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE), new CardImpl(HEARTS, SIX),
                new CardImpl(HEARTS, SEVEN), new CardImpl(HEARTS, EIGHT),
                new CardImpl(HEARTS, NINE)
        ));

        final CardImpl six = new CardImpl(SPADES, SIX);
        final CardImpl spare = new CardImpl(SPADES, EIGHT);
        player.addCardHand(six);
        player.addCardHand(spare);

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(SPADES, THREE),
                new CardImpl(SPADES, FOUR),
                new CardImpl(SPADES, FIVE)
        )));
        final List<Card> selected = new ArrayList<>(List.of(six));
        controller.tryAttach(player, selected, combo, true, true);

        assertFalse(player.hasCard(six));
        assertTrue(player.hasCard(spare));
    }

    @Test
    void testSuccessBurraco() {
        player.setInPot(true);

        final CardImpl nineSpades = new CardImpl(SPADES, NINE);
        final CardImpl spare = new CardImpl(SPADES, KING);
        player.addCardHand(nineSpades);
        player.addCardHand(spare);

        final List<Card> combo = new ArrayList<>(List.of(
                new CardImpl(SPADES, THREE),
                new CardImpl(SPADES, FOUR),
                new CardImpl(SPADES, FIVE),
                new CardImpl(SPADES, SIX),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(SPADES, EIGHT)
        ));

        final List<Card> selected = new ArrayList<>(List.of(nineSpades));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.SUCCESS_BURRACO, result);
    }

    @Test
    void testSuccessTakePot() {
        player.setInPot(false);

        final CardImpl six = new CardImpl(HEARTS, SIX);
        player.addCardHand(six);

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        )));
        final List<Card> selected = new ArrayList<>(List.of(six));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.SUCCESS_TAKE_POT, result);
    }
}
