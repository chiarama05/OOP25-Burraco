package it.unibo.burraco.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscardPileTest {

    private DiscardPile discardPile;

    private Card makeCard(final Seed seed, final CardValue value) {
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
        final Card c = this.makeCard(Seed.HEARTS, CardValue.ACE);
        this.discardPile.add(c);

        final List<Card> cards = this.discardPile.getCards();
        assertEquals(1, cards.size());
        assertEquals(c, cards.get(cards.size() - 1));
    }

    @Test
    void testTakeAll() {
        this.discardPile.add(new CardImpl(Seed.HEARTS, CardValue.FIVE));
        this.discardPile.add(new CardImpl(Seed.CLUBS, CardValue.QUEEN));

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
        this.discardPile.add(this.makeCard(Seed.SPADES, CardValue.JACK));
        this.discardPile.add(this.makeCard(Seed.HEARTS, CardValue.TEN));

        assertFalse(this.discardPile.isEmpty());
        this.discardPile.reset();

        assertTrue(this.discardPile.isEmpty());
        assertEquals(0, this.discardPile.getCards().size());
    }
}
