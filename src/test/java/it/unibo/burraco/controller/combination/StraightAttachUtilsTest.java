package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.combination.straight.StraightAttachUtils;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class StraightAttachUtilsTest {

    private static final String HEARTS = "H";
    private static final String DIAMONDS = "D";
    private static final String SPADES = "S";
    private static final String CLUBS = "C";
    private static final String JOLLY = "Jolly";
    private static final String TWO = "2";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";
    private static final String SEVEN = "7";
    private static final String NINE = "9";
    private static final String TEN = "10";
    private static final String JACK = "J";
    private static final String QUEEN = "Q";
    private static final String KING = "K";
    private static final String ACE = "A";

    private StraightAttachUtils attachUtils;

    @BeforeEach
    void setUp() {
        // Inizializziamo l'istanza prima di ogni test
        this.attachUtils = new StraightAttachUtils();
    }

    private Card card(final String seed, final String value) {
        return new CardImpl(seed, value);
    }

    @Test
    void testAppend() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(HEARTS, THREE), card(HEARTS, FOUR), card(HEARTS, FIVE)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, SIX)));
    }

    @Test
    void testPrependNaturalTwo() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(HEARTS, THREE), card(HEARTS, FOUR), card(HEARTS, FIVE)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, TWO)));
    }

    @Test
    void testAppendAceHigh() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(HEARTS, JACK), card(HEARTS, QUEEN), card(HEARTS, KING)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, ACE)));
    }

    @Test
    void testMultipleCards() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(DIAMONDS, THREE), card(DIAMONDS, FOUR), card(DIAMONDS, FIVE)
        ));
        final List<Card> newCards = List.of(card(DIAMONDS, SIX), card(DIAMONDS, SEVEN));
        assertTrue(attachUtils.canAttachToStraight(straight, newCards));
    }

    @Test
    void testTrailingJokerPushedForward() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(HEARTS, THREE), card(HEARTS, FOUR), card(JOLLY, JOLLY)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, FIVE)));
    }

    @Test
    void testLeadingJokerPushedBackward() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(JOLLY, JOLLY), card(HEARTS, FOUR), card(HEARTS, FIVE)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, THREE)));
    }

    @Test
    void testTrailingPinellaPushedForward() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(HEARTS, THREE), card(HEARTS, FOUR), card(SPADES, TWO)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, FIVE)));
    }

    @Test
    void testJollyReplacement() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(SPADES, FIVE), card(JOLLY, JOLLY), card(SPADES, SEVEN)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(SPADES, SIX)));
    }

    @Test
    void testPinellaReplacement() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(SPADES, NINE), card(HEARTS, TWO), card(SPADES, JACK)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(SPADES, TEN)));
    }

    @Test
    void testKingInQKA() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(CLUBS, QUEEN), card(JOLLY, JOLLY), card(CLUBS, ACE)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(CLUBS, KING)));
    }

    @Test
    void testAttachAfterInternalJoker() {
        final List<Card> straight = new ArrayList<>(List.of(
            card(HEARTS, THREE), card(JOLLY, JOLLY), card(HEARTS, FIVE)
        ));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, SIX)));
    }

    @Test
    void testWrongSeed() {
        final List<Card> straight = List.of(card(HEARTS, THREE), card(HEARTS, FOUR));
        assertFalse(attachUtils.canAttachToStraight(straight, card(SPADES, FIVE)));
    }

    @Test
    void testGap() {
        final List<Card> straight = List.of(card(HEARTS, THREE), card(HEARTS, FOUR));
        assertFalse(attachUtils.canAttachToStraight(straight, card(HEARTS, SEVEN)));
    }

    @Test
    void testSubstitutionWrongSeed() {
        final List<Card> straight = List.of(
            card(DIAMONDS, FIVE), card(JOLLY, JOLLY), card(DIAMONDS, SEVEN));
        assertFalse(attachUtils.canAttachToStraight(straight, card(HEARTS, SIX)));
    }

    @Test
    void testAceLow() {
        final List<Card> straight = List.of(
            card(HEARTS, ACE), card(HEARTS, TWO), card(HEARTS, THREE));
        assertTrue(attachUtils.canAttachToStraight(straight, card(HEARTS, FOUR)));
    }

    @Test
    void testEmptyList() {
        final List<Card> straight = List.of(card(HEARTS, THREE), card(HEARTS, FOUR));
        assertFalse(attachUtils.canAttachToStraight(straight, new ArrayList<>()));
    }
}
