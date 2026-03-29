package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.*;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class StraightAttachUtilsTest {

    private Card card(String seed, String value) {
        return new CardImpl(seed, value);
    }

    @Nested
    @DisplayName("Standard Extension")
    class StandardExtension {
        
        @Test
        @DisplayName("Appends 6H to 3-4-5 Hearts")
        void testAppend() {
            List<Card> straight = new ArrayList<>(List.of(
                card("H", "3"), card("H", "4"), card("H", "5")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "6")));
        }

        @Test
        @DisplayName("Prepends 2H (natural two) before 3H in 3-4-5 Hearts")
        void testPrependNaturalTwo() {
            List<Card> straight = new ArrayList<>(List.of(
                card("H", "3"), card("H", "4"), card("H", "5")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "2")));
        }

        @Test
        @DisplayName("Extends Ace-high straight: J-Q-K Hearts + AH")
        void testAppendAceHigh() {
            List<Card> straight = new ArrayList<>(List.of(
                card("H", "J"), card("H", "Q"), card("H", "K")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "A")));
        }

        @Test
        @DisplayName("Appends multiple cards: 3-4-5 Diamonds + [6D, 7D]")
        void testMultipleCards() {
            List<Card> straight = new ArrayList<>(List.of(
                card("D", "3"), card("D", "4"), card("D", "5")
            ));
            List<Card> newCards = List.of(card("D", "6"), card("D", "7"));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, newCards));
        }
    }


    @Nested
    @DisplayName("Wildcard at border")
    class WildcardAtBorder {

        @Test
        @DisplayName("Appends 5H to [3H, 4H, Joker]: joker shifts one position forward")
        void testTrailingJokerPushedForward() {
            List<Card> straight = new ArrayList<>(List.of(
                card("H", "3"), card("H", "4"), card("Jolly", "Jolly")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "5")));
        }

        @Test
        @DisplayName("Prepends 3H to [Joker, 4H, 5H]: joker shifts one position backward")
        void testLeadingJokerPushedBackward() {
            List<Card> straight = new ArrayList<>(List.of(
                card("Jolly", "Jolly"), card("H", "4"), card("H", "5")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "3")));
        }

        @Test
        @DisplayName("Appends 5H to [3H, 4H, Pinella]: pinella (2S) shifts forward")
        void testTrailingPinellaPushedForward() {
            List<Card> straight = new ArrayList<>(List.of(
                card("H", "3"), card("H", "4"), card("S", "2")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "5")));
        }
    }


    @Nested
    @DisplayName("Internal Wildcard Substitution")
    class WildcardSubstitution {

        @Test
        @DisplayName("Replaces jolly between 5S and 7S with 6S")
        void testJollyReplacement() {
            List<Card> straight = new ArrayList<>(List.of(
                card("S", "5"), card("Jolly", "Jolly"), card("S", "7")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("S", "6")));
        }

        @Test
        @DisplayName("Replaces Pinella between 9S and JS with 10S")
        void testPinellaReplacement() {
            List<Card> straight = new ArrayList<>(List.of(
                card("S", "9"), card("H", "2"), card("S", "J")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("S", "10")));
        }

        @Test
        @DisplayName("special case: King replaces jolly in Q-jolly-A ")
        void testKingInQKA() {
            List<Card> straight = new ArrayList<>(List.of(
                card("C", "Q"), card("Jolly", "Jolly"), card("C", "A")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("C", "K")));
        }

        @Test
        @DisplayName("6H is attached normally to [3H, Joker, 5H]: jolly stays in place")
        void testAttachAfterInternalJoker() {
            // [3H, Joker, 5H, 6H] è una scala valida: il joker rimane sul 4
            List<Card> straight = new ArrayList<>(List.of(
                card("H", "3"), card("Jolly", "Jolly"), card("H", "5")
            ));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "6")));
        }
    }


    @Nested
    @DisplayName("Negative Cases")
    class NegativeTests {

        @Test
        @DisplayName("Rejects card with wrong seed")
        void testWrongSeed() {
            List<Card> straight = List.of(card("H", "3"), card("H", "4"));
            assertFalse(StraightAttachUtils.canAttachToStraight(straight, card("S", "5")));
        }

        @Test
        @DisplayName("Reject non-consecutive value")
        void testGap() {
            List<Card> straight = List.of(card("H", "3"), card("H", "4"));
            assertFalse(StraightAttachUtils.canAttachToStraight(straight, card("H", "7")));
        }

        @Test
        @DisplayName("Rejects substitution when replacement card has wrong seed")
        void testSubstitutionWrongSeed() {
            List<Card> straight = List.of(card("D", "5"), card("Jolly", "Jolly"), card("D", "7"));
            assertFalse(StraightAttachUtils.canAttachToStraight(straight, card("H", "6")));
        }
    }

    
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Ace-low straight A-2-3 Hearts extended with 4H")
        void testAceLow() {
            List<Card> straight = List.of(card("H", "A"), card("H", "2"), card("H", "3"));
            assertTrue(StraightAttachUtils.canAttachToStraight(straight, card("H", "4")));
        }

        @Test
        @DisplayName("Reject empty list")
        void testEmptyList() {
            List<Card> straight = List.of(card("H", "3"), card("H", "4"));
            assertFalse(StraightAttachUtils.canAttachToStraight(straight, new ArrayList<>()));
        }
    }
}