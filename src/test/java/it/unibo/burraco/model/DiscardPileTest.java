package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;

class DiscardPileTest {

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
        assertTrue(discardPile.isEmpty());
        assertEquals(0, discardPile.getCards().size());
    }

    @Test
    void testAddSingleCard() {
        final int initialSize = discardPile.getCards().size();
        final Card c = makeCard("♥", "A");
        discardPile.add(c);
        assertEquals(initialSize + 1, discardPile.getCards().size());
        final List<Card> cards = discardPile.getCards();
        assertEquals(c, cards.get(cards.size() - 1));
    }

    @Test
    void testTakeAll() {
        discardPile.add(new CardImpl("♥", "5"));
        discardPile.add(new CardImpl("♣", "Q"));
        final List<Card> allCards = discardPile.getCards();
        assertEquals(2, allCards.size());
        discardPile.reset();
        assertTrue(discardPile.isEmpty());
    }

    @Test
    void testDrawFromEmptyPile() {
        assertNull(discardPile.drawLast());
    }

    @Test
    void testReset() {
        discardPile.add(makeCard("♠", "J"));
        discardPile.add(makeCard("♥", "10"));  
        assertFalse(discardPile.isEmpty());
        discardPile.reset();
        assertTrue(discardPile.isEmpty());
        assertEquals(0, discardPile.getCards().size());
    }
}
