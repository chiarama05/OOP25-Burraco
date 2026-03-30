package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.*;

import it.unibo.burraco.controller.combination.straight.StraightAttachUtils;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class StraightAttachUtilsTest {

    private Card card(String seed, String value) {
        return new CardImpl(seed, value);
    }

    @Test
    void testAppend() {
        List<Card> straight = new ArrayList<>(List.of(
            card("H", "3"), card("H", "4"), card("H", "5")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "6")));
    }

    @Test
    void testPrependNaturalTwo() {
        List<Card> straight = new ArrayList<>(List.of(
            card("H", "3"), card("H", "4"), card("H", "5")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "2")));
    }

    @Test
    void testAppendAceHigh() {
        List<Card> straight = new ArrayList<>(List.of(
            card("H", "J"), card("H", "Q"), card("H", "K")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "A")));
    }

    @Test
    void testMultipleCards() {
        List<Card> straight = new ArrayList<>(List.of(
            card("D", "3"), card("D", "4"), card("D", "5")
        ));
        List<Card> newCards = List.of(card("D", "6"), card("D", "7"));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, newCards));
    }


    @Test
    void testTrailingJokerPushedForward() {
        List<Card> straight = new ArrayList<>(List.of(
            card("H", "3"), card("H", "4"), card("Jolly", "Jolly")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "5")));
    }

    @Test
    void testLeadingJokerPushedBackward() {
        List<Card> straight = new ArrayList<>(List.of(
            card("Jolly", "Jolly"), card("H", "4"), card("H", "5")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "3")));
    }

    @Test
    void testTrailingPinellaPushedForward() {
        List<Card> straight = new ArrayList<>(List.of(
            card("H", "3"), card("H", "4"), card("S", "2")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "5")));
    }

    @Test
    void testJollyReplacement() {
        List<Card> straight = new ArrayList<>(List.of(
            card("S", "5"), card("Jolly", "Jolly"), card("S", "7")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("S", "6")));
    }

    @Test
    void testPinellaReplacement() {
        List<Card> straight = new ArrayList<>(List.of(
            card("S", "9"), card("H", "2"), card("S", "J")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("S", "10")));
    }

    @Test
    void testKingInQKA() {
        List<Card> straight = new ArrayList<>(List.of(
            card("C", "Q"), card("Jolly", "Jolly"), card("C", "A")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("C", "K")));
    }

    @Test
    void testAttachAfterInternalJoker() {
        List<Card> straight = new ArrayList<>(List.of(
            card("H", "3"), card("Jolly", "Jolly"), card("H", "5")
        ));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "6")));
    }

    @Test
    void testWrongSeed() {
        List<Card> straight = List.of(card("H", "3"), card("H", "4"));
        assertFalse(StraightAttachUtils.canAttachToStraight(straight, card("S", "5")));
    }

    @Test
    void testGap() {
        List<Card> straight = List.of(card("H", "3"), card("H", "4"));
        assertFalse(StraightAttachUtils.canAttachToStraight(straight, card("H", "7")));
    }

    @Test
    void testSubstitutionWrongSeed() {
        List<Card> straight = List.of(card("D", "5"), card("Jolly", "Jolly"), card("D", "7"));
        assertFalse(StraightAttachUtils.canAttachToStraight(straight, card("H", "6")));
    }

    @Test
    void testAceLow() {
        List<Card> straight = List.of(card("H", "A"), card("H", "2"), card("H", "3"));
        assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "4")));
    }

    @Test
    void testEmptyList() {
        List<Card> straight = List.of(card("H", "3"), card("H", "4"));
        assertFalse(StraightAttachUtils.canAttachToStraight(straight, new ArrayList<>()));
    }
}
