package it.unibo.burraco.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.controller.attach.AttachUtils;
import it.unibo.burraco.model.card.CardImpl;

public class AttachUtilsTest {
    private static final String HEARTS  = "♥";
    private static final String SPADES  = "♠";
    private static final String CLUBS   = "♣";
    private static final String DIAMONDS = "♦";
    private static final String JOLLY_SEED = "♕";

    // ── Casi limite ──────────────────────────────────────────────────────────

    @Test
    void testNullCombinationReturnsFalse() {
        final Card card = new CardImpl(HEARTS, "5");
        assertFalse(AttachUtils.canAttach(null, card));
    }

    @Test
    void testEmptyCombinationReturnsFalse() {
        final Card card = new CardImpl(HEARTS, "5");
        assertFalse(AttachUtils.canAttach(new ArrayList<>(), card));
    }

    // ── Attacco su Tris (Set) ────────────────────────────────────────────────

    @Test
    void testCanAttachSameValueToSet() {
        // Tris di 7: aggiunta del quarto 7 → valida
        final List<Card> set = List.of(
                new CardImpl(HEARTS,   "7"),
                new CardImpl(SPADES,   "7"),
                new CardImpl(CLUBS,    "7")
        );
        final Card newCard = new CardImpl(DIAMONDS, "7");
        assertTrue(AttachUtils.canAttach(set, newCard));
    }

    @Test
    void testCannotAttachDifferentValueToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, "7"),
                new CardImpl(SPADES, "7"),
                new CardImpl(CLUBS,  "7")
        );
        final Card newCard = new CardImpl(DIAMONDS, "8");
        assertFalse(AttachUtils.canAttach(set, newCard));
    }

    @Test
    void testCanAttachJollyToSetWithNoWildcard() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, "7"),
                new CardImpl(SPADES, "7"),
                new CardImpl(CLUBS,  "7")
        );
        final Card jolly = new CardImpl(JOLLY_SEED, "Jolly");
        assertTrue(AttachUtils.canAttach(set, jolly));
    }

    @Test
    void testCannotAttachSecondWildcardToSet() {
        // Il tris ha già un Jolly → non si può aggiungere un altro wildcard
        final List<Card> set = List.of(
                new CardImpl(HEARTS,    "7"),
                new CardImpl(SPADES,    "7"),
                new CardImpl(JOLLY_SEED,"Jolly")
        );
        final Card jolly2 = new CardImpl(JOLLY_SEED, "Jolly");
        assertFalse(AttachUtils.canAttach(set, jolly2));
    }

    @Test
    void testCanAttachNaturalCardToSetWithWildcard() {
        // Tris con Jolly: aggiunta del 7 reale è ancora valida
        final List<Card> set = List.of(
                new CardImpl(HEARTS,     "7"),
                new CardImpl(SPADES,     "7"),
                new CardImpl(JOLLY_SEED, "Jolly")
        );
        final Card newCard = new CardImpl(DIAMONDS, "7");
        assertTrue(AttachUtils.canAttach(set, newCard));
    }

    // ── Attacco su Scala (Straight) ──────────────────────────────────────────

    @Test
    void testCanAttachConsecutiveCardToStraight() {
        // Scala 3-4-5 di cuori → aggiunta 6♥
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        ));
        final Card newCard = new CardImpl(HEARTS, "6");
        assertTrue(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCanAttachAtBeginningOfStraight() {
        // Scala 4-5-6 di cuori → aggiunta 3♥ in testa
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5"),
                new CardImpl(HEARTS, "6")
        ));
        final Card newCard = new CardImpl(HEARTS, "3");
        assertTrue(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCannotAttachNonConsecutiveCardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        ));
        final Card newCard = new CardImpl(HEARTS, "9");
        assertFalse(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCannotAttachWrongSeedToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        ));
        final Card newCard = new CardImpl(SPADES, "6");
        assertFalse(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCanAttachJollyToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, "3"),
                new CardImpl(HEARTS, "4"),
                new CardImpl(HEARTS, "5")
        ));
        final Card jolly = new CardImpl(JOLLY_SEED, "Jolly");
        assertTrue(AttachUtils.canAttach(straight, jolly));
    }

    @Test
    void testCannotAttachSecondWildcardToStraight() {
        // Scala con Jolly già presente → secondo wildcard non ammesso
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS,     "3"),
                new CardImpl(JOLLY_SEED, "Jolly"),
                new CardImpl(HEARTS,     "5")
        ));
        final Card jolly2 = new CardImpl(JOLLY_SEED, "Jolly");
        assertFalse(AttachUtils.canAttach(straight, jolly2));
    }
}
