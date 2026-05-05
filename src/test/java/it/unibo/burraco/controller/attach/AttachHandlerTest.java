package it.unibo.burraco.controller.attach;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class AttachHandlerTest {

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

    private AttachHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new AttachHandler();
    }

    @Test
    void testNullCombinationReturnsFalse() {
        final Card card = new CardImpl(HEARTS, FIVE);
        assertFalse(handler.canAttach(null, card));
    }

    @Test
    void testCanAttachSameValueToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(CLUBS, SEVEN)
        );
        final Card newCard = new CardImpl(DIAMONDS, SEVEN);
        assertTrue(handler.canAttach(set, newCard));
    }

    @Test
    void testCannotAttachDifferentValueToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(CLUBS, SEVEN)
        );
        final Card newCard = new CardImpl(DIAMONDS, "8");
        assertFalse(handler.canAttach(set, newCard));
    }

    @Test
    void testCanAttachJollyToSetWithNoWildcard() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(CLUBS, SEVEN)
        );
        final Card jolly = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertTrue(handler.canAttach(set, jolly));
    }

    @Test
    void testCannotAttachSecondWildcardToSet() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(JOLLY_SEED, JOLLY_VALUE)
        );
        final Card jolly2 = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertFalse(handler.canAttach(set, jolly2));
    }

    @Test
    void testCanAttachNaturalCardToSetWithWildcard() {
        final List<Card> set = List.of(
                new CardImpl(HEARTS, SEVEN),
                new CardImpl(SPADES, SEVEN),
                new CardImpl(JOLLY_SEED, JOLLY_VALUE)
        );
        final Card newCard = new CardImpl(DIAMONDS, SEVEN);
        assertTrue(handler.canAttach(set, newCard));
    }

    @Test
    void testCanAttachConsecutiveCardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card newCard = new CardImpl(HEARTS, SIX);
        assertTrue(handler.canAttach(straight, newCard));
    }

    @Test
    void testCanAttachAtBeginningOfStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE),
                new CardImpl(HEARTS, SIX)
        ));
        final Card newCard = new CardImpl(HEARTS, THREE);
        assertTrue(handler.canAttach(straight, newCard));
    }

    @Test
    void testCannotAttachNonConsecutiveCardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card newCard = new CardImpl(HEARTS, "9");
        assertFalse(handler.canAttach(straight, newCard));
    }

    @Test
    void testCannotAttachWrongSeedToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card newCard = new CardImpl(SPADES, SIX);
        assertFalse(handler.canAttach(straight, newCard));
    }

    @Test
    void testCanAttachJollyToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(HEARTS, FOUR),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card jolly = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertTrue(handler.canAttach(straight, jolly));
    }

    @Test
    void testCannotAttachSecondWildcardToStraight() {
        final List<Card> straight = new ArrayList<>(List.of(
                new CardImpl(HEARTS, THREE),
                new CardImpl(JOLLY_SEED, JOLLY_VALUE),
                new CardImpl(HEARTS, FIVE)
        ));
        final Card jolly2 = new CardImpl(JOLLY_SEED, JOLLY_VALUE);
        assertFalse(handler.canAttach(straight, jolly2));
    }
}
