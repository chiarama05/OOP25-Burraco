package it.unibo.burraco.controller.attach;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class AttachUtilsTest {
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String CLUBS = "♣";
    private static final String DIAMONDS = "♦";
    private static final String JOLLY_SEED = "♕";
    private static final String JOLLY_VALUE = "Jolly";
    private static final String SEVEN = "7";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";

    @Test
    void testNullCombinationReturnsFalse() {
        final Card card = new CardImpl(HEARTS, FIVE);
        assertFalse(AttachUtils.canAttach(null, card));
    }

    @Test
    void testCanAttachSameValueToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(CLUBS, SEVEN)
        );
        final Card newCard = new CardImpl(DIAMONDS, SEVEN);
        assertTrue(AttachUtils.canAttach(set, newCard));
    }

    @Test
    void testCannotAttachDifferentValueToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(CLUBS, SEVEN)
        );
        final Card newCard = new CardImpl(DIAMONDS, "8");
        assertFalse(AttachUtils.canAttach(set, newCard));
    }

    @Test
    void testCanAttachJollyToSetWithNoWildcard() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(CLUBS, SEVEN)
        );
        final Card jolly = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertTrue(AttachUtils.canAttach(set, jolly));
    }

    @Test
    void testCannotAttachSecondWildcardToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(JOLLY_SEED, JOLLY_VALUE)
        );
        final Card jolly2 = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertFalse(AttachUtils.canAttach(set, jolly2));
    }

    @Test
    void testCanAttachNaturalCardToSetWithWildcard() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(JOLLY_SEED, JOLLY_VALUE)
        );
        final Card newCard = new CardImpl(DIAMONDS, SEVEN);
        assertTrue(AttachUtils.canAttach(set, newCard));
    }

    @Test
    void testCanAttachConsecutiveCardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card newCard = new CardImpl(HEARTS, SIX);
        assertTrue(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCanAttachAtBeginningOfStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE),
                new CardImpl(HEARTS, SIX)
        ));
        final Card newCard = new CardImpl(HEARTS, THREE);
        assertTrue(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCannotAttachNonConsecutiveCardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card newCard = new CardImpl(HEARTS, "9");
        assertFalse(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCannotAttachWrongSeedToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card newCard = new CardImpl(SPADES, SIX);
        assertFalse(AttachUtils.canAttach(straight, newCard));
    }

    @Test
    void testCanAttachJollyToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card jolly = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertTrue(AttachUtils.canAttach(straight, jolly));
    }

    @Test
    void testCannotAttachSecondWildcardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(JOLLY_SEED, JOLLY_VALUE),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card jolly2 = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertFalse(AttachUtils.canAttach(straight, jolly2));
    }
}
