package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.combination.set.SetHandler;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class SetHandlerTest {

    private static final String TEN = "10";
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String DIAMONDS = "♦";
    private static final String CLUBS = "♣";

    private SetHandler setHandler;

    @BeforeEach
    void setUp() {
        this.setHandler = new SetHandler();
    }

    @Test
    void testCanAttachSameValue() {
        final List<Card> set = new ArrayList<>(List.of(
            new CardImpl(HEARTS, TEN),
            new CardImpl(SPADES, TEN),
            new CardImpl(DIAMONDS, TEN)
        ));
        final Card newTen = new CardImpl(CLUBS, TEN);
        assertTrue(setHandler.canAttach(set, newTen));
    }

    @Test
    void testCannotAttachSecondWildcard() {
        final List<Card> set = new ArrayList<>(List.of(
            new CardImpl(HEARTS, TEN),
            new CardImpl(SPADES, TEN),
            new CardImpl("Jolly", "Jolly")
        ));
        final Card two = new CardImpl(DIAMONDS, "2");
        assertFalse(setHandler.canAttach(set, two));
    }
}
