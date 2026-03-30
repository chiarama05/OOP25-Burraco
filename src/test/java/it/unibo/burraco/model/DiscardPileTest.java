package it.unibo.burraco.model;

import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

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
    void testAddCard() {
        final Card c = makeCard("♥", "A");
        discardPile.add(c);
        
        assertFalse(discardPile.isEmpty());
        assertEquals(1, discardPile.getCards().size());
        assertEquals(c, discardPile.getCards().get(0));
    }

    @Test
    void testAddAll() {
        final List<Card> cards = List.of(
            makeCard("♥", "A"),
            makeCard("♦", "K"),
            makeCard("♣", "Q")
        );
        
        discardPile.addAll(cards);
        assertEquals(3, discardPile.getCards().size());
        assertFalse(discardPile.isEmpty());
    }

    @Test
    void testDrawLast() {
        final Card c1 = makeCard("♥", "A");
        final Card c2 = makeCard("♣", "2");
        
        discardPile.add(c1);
        discardPile.add(c2);
        

        final Card drawn = discardPile.drawLast();
        assertEquals(c2, drawn);
        assertEquals(1, discardPile.getCards().size());
        

        assertEquals(c1, discardPile.drawLast());
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
