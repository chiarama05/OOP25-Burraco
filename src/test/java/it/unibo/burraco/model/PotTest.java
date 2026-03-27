package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

import java.util.List;

/**
 * Gestione pozzetti: addToPot, drawPot, flag isInPot,
 * interazione con la mano e reset.
 */
public class PotTest {

    private Player player;

    private static final List<Card> POT_11 = List.of(
            new CardImpl("♠", "A"), new CardImpl("♠", "2"),
            new CardImpl("♠", "3"), new CardImpl("♠", "4"),
            new CardImpl("♠", "5"), new CardImpl("♠", "6"),
            new CardImpl("♠", "7"), new CardImpl("♠", "8"),
            new CardImpl("♠", "9"), new CardImpl("♠", "10"),
            new CardImpl("♠", "J"));

    @BeforeEach
    void setUp() {
        player = new PlayerImpl("Tester");
    }

    // ── Stato iniziale ────────────────────────────────────────────────────────

    @Test
    void testNotInPotAtCreation() {
        assertFalse(player.isInPot());
    }

    @Test
    void testDrawPotOnEmptyPotIsNoop() {
        player.drawPot();
        assertTrue(player.getHand().isEmpty());
        assertFalse(player.isInPot());
    }

    // ── addToPot() ────────────────────────────────────────────────────────────

    @Test
    void testAddToPotThenDrawMovesAllCards() {
        player.addToPot(POT_11);
        player.drawPot();
        assertEquals(POT_11.size(), player.getHand().size());
    }

    @Test
    void testAddToPotReplacesPreviousPot() {
        player.addToPot(List.of(new CardImpl("♥", "3"), new CardImpl("♥", "4")));
        player.addToPot(List.of(new CardImpl("♦", "K"))); // sostituisce
        player.drawPot();
        assertEquals(1, player.getHand().size());
    }

    // ── drawPot() ─────────────────────────────────────────────────────────────

    @Test
    void testDrawPotSetsInPotTrue() {
        player.addToPot(POT_11);
        player.drawPot();
        assertTrue(player.isInPot());
    }

    @Test
    void testDrawPotEmptiesPot() {
        player.addToPot(POT_11);
        player.drawPot();
        int afterFirst = player.getHand().size();
        player.drawPot(); // seconda chiamata: pot vuoto
        assertEquals(afterFirst, player.getHand().size());
    }

    @Test
    void testDrawPotAddsToExistingHand() {
        Card existing = new CardImpl("♦", "Q");
        player.addCardHand(existing);
        player.addToPot(POT_11);
        player.drawPot();
        assertEquals(1 + POT_11.size(), player.getHand().size());
        assertTrue(player.getHand().contains(existing));
    }

    @Test
    void testDrawPotWith11CardsPerRules() {
        player.addToPot(POT_11);
        player.drawPot();
        assertEquals(11, player.getHand().size());
    }

    // ── isInPot flag ──────────────────────────────────────────────────────────

    @Test
    void testSetInPotManually() {
        player.setInPot(true);
        assertTrue(player.isInPot());
        player.setInPot(false);
        assertFalse(player.isInPot());
    }

    @Test
    void testIsInPotFalseWhenPotWasEmpty() {
        player.drawPot(); // pot vuoto → no-op
        assertFalse(player.isInPot());
    }

    // ── hasFinishedCards() interazione ────────────────────────────────────────

    @Test
    void testHasFinishedCardsTrueBeforeDrawingPot() {
        player.addToPot(POT_11);
        assertTrue(player.hasFinishedCards()); // mano ancora vuota
    }

    @Test
    void testHasFinishedCardsFalseAfterDrawingPot() {
        player.addToPot(POT_11);
        player.drawPot();
        assertFalse(player.hasFinishedCards());
    }

    // ── Reset ─────────────────────────────────────────────────────────────────

    @Test
    void testResetClearsPotAndFlag() {
        player.addToPot(POT_11);
        player.drawPot();
        player.resetForNewRound();
        assertFalse(player.isInPot());
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void testResetThenDrawPotIsNoop() {
        player.addToPot(POT_11);
        player.drawPot();
        player.resetForNewRound();
        player.drawPot(); // pot svuotato dal reset
        assertTrue(player.getHand().isEmpty());
    }
}
