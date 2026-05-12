package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.cards.DiscardPileImpl;

class DiscardPileTest {

    private static final String HEARTS = "♥";

    private DiscardPile discardPile;

    private Card makeCard(final String seed, final String value) {
        return new CardImpl(seed, value);
    }

    @BeforeEach
    void init() {
        this.discardPile = new DiscardPileImpl();
    }

    @Test
    void testInitialState() {
        assertTrue(this.discardPile.isEmpty());
        assertEquals(0, this.discardPile.getCards().size());
    }

    @Test
    void testAddSingleCard() {
        final Card c = this.makeCard(HEARTS, "A");
        this.discardPile.add(c);

        final List<Card> cards = this.discardPile.getCards();
        assertEquals(1, cards.size());
        assertEquals(c, cards.get(cards.size() - 1));
    }

    @Test
    void testTakeAll() {
        this.discardPile.add(new CardImpl(HEARTS, "5"));
        this.discardPile.add(new CardImpl("♣", "Q"));

        final List<Card> allCards = this.discardPile.getCards();
        assertEquals(2, allCards.size());

        this.discardPile.reset();

        assertTrue(this.discardPile.isEmpty());
        assertEquals(0, this.discardPile.getCards().size());
    }

    @Test
    void testDrawFromEmptyPile() {
        assertNull(this.discardPile.drawLast());
    }

    @Test
    void testReset() {
        this.discardPile.add(this.makeCard("♠", "J"));
        this.discardPile.add(this.makeCard(HEARTS, "10"));

        assertFalse(this.discardPile.isEmpty());
        this.discardPile.reset();

        assertTrue(this.discardPile.isEmpty());
        assertEquals(0, this.discardPile.getCards().size());
    }
}
