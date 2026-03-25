package it.unibo.burraco.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.attach.AttachController;
import it.unibo.burraco.controller.attach.AttachResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;

public class AttachControllerTest {
    private static final String HEARTS   = "♥";
    private static final String SPADES   = "♠";

    private AttachController controller;
    private PlayerImpl player;

    @BeforeEach
    void init() {
        this.controller = new AttachController();
        this.player     = new PlayerImpl("TestPlayer");
    }

    private List<Card> setupCombination(List<Card> cards) {
        player.addCombination(cards);
        return player.getCombinations().get(player.getCombinations().size() - 1);
    }

    @Test
    void testNotDrawn() {
        final List<Card> selected = List.of(new CardImpl(HEARTS, "6"));
        final List<Card> combo    = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        )));

        final AttachResult result = controller.tryAttach(player, selected, combo, false, true);
        assertEquals(AttachResult.NOT_DRAWN, result);
    }

    @Test
    void testWrongPlayer() {
        final List<Card> selected = List.of(new CardImpl(HEARTS, "6"));
        final List<Card> combo    = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        )));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, false);
        assertEquals(AttachResult.WRONG_PLAYER, result);
    }

    @Test
    void testNoCardsSelected() {
        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        )));

        final AttachResult result = controller.tryAttach(player, new ArrayList<>(), combo, true, true);
        assertEquals(AttachResult.NO_CARDS_SELECTED, result);
    }

    @Test
    void testInvalidCombination() {
        // Scala di cuori + carta di picche non consecutiva: combinazione non valida
        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        )));
        final List<Card> selected = List.of(new CardImpl(SPADES, "9"));
        player.addCardHand(selected.get(0));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.INVALID_COMBINATION, result);
    }

    @Test
    void testWouldGetStuckAfterAttach() {
        // Giocatore ha il pozzetto, ha in mano SOLO la carta che vuole attaccare
        // → resterebbe a 0 carte → WOULD_GET_STUCK
        player.setInPot(true);

        final CardImpl six = new CardImpl(HEARTS, "6");
        player.addCardHand(six);

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        )));
        final List<Card> selected = new ArrayList<>(List.of(six));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.WOULD_GET_STUCK, result);
    }

    @Test
    void testSuccess() {
        // Giocatore ha pozzetto, ha 2 carte in mano, ne attacca 1 → resta con 1 ma ha burraco
        player.setInPot(true);

        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));

        final CardImpl sixSpades = new CardImpl(SPADES, "6");
        final CardImpl sevenSpades = new CardImpl(SPADES, "7");
        player.addCardHand(sixSpades);
        player.addCardHand(sevenSpades); // carta di riserva: resta con 1 dopo l'attacco

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(SPADES, "3"),
                new CardImpl(SPADES, "4"),
                new CardImpl(SPADES, "5")
        )));
        final List<Card> selected = new ArrayList<>(List.of(sixSpades));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.SUCCESS, result);
    }

    @Test
    void testSuccessRemovesCardFromHand() {
        player.setInPot(true);

        player.addCombination(List.of(
                new CardImpl(HEARTS, "3"), new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"), new CardImpl(HEARTS, "6"),
                new CardImpl(HEARTS, "7"), new CardImpl(HEARTS, "8"),
                new CardImpl(HEARTS, "9")
        ));

        final CardImpl six = new CardImpl(SPADES, "6");
        final CardImpl spare = new CardImpl(SPADES, "8");
        player.addCardHand(six);
        player.addCardHand(spare);

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(SPADES, "3"),
                new CardImpl(SPADES, "4"),
                new CardImpl(SPADES, "5")
        )));
        final List<Card> selected = new ArrayList<>(List.of(six));
        controller.tryAttach(player, selected, combo, true, true);

        assertFalse(player.hasCard(six));
        assertTrue(player.hasCard(spare));
    }

    @Test
    void testSuccessBurraco() {
        player.setInPot(true);

        final CardImpl nineSpades = new CardImpl(SPADES, "9");
        final CardImpl spare      = new CardImpl(SPADES, "K");
        player.addCardHand(nineSpades);
        player.addCardHand(spare);

        final List<Card> combo = new ArrayList<>(List.of(
            new CardImpl(SPADES, "3"),
            new CardImpl(SPADES, "4"),
            new CardImpl(SPADES, "5"),
            new CardImpl(SPADES, "6"),
            new CardImpl(SPADES, "7"),
            new CardImpl(SPADES, "8")
        ));

        final List<Card> selected = new ArrayList<>(List.of(nineSpades));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.SUCCESS_BURRACO, result);
    }

    @Test
    void testSuccessTakePot() {
        // Giocatore NON ha ancora il pozzetto, rimane a 0 carte dopo l'attacco
        // deve prendere il pozzetto
        player.setInPot(false);

        final CardImpl six = new CardImpl(HEARTS, "6");
        player.addCardHand(six); 

        final List<Card> combo = setupCombination(new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        )));
        final List<Card> selected = new ArrayList<>(List.of(six));

        final AttachResult result = controller.tryAttach(player, selected, combo, true, true);
        assertEquals(AttachResult.SUCCESS_TAKE_POT, result);
    }
}
