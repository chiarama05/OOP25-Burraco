package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.DeckImpl;

class DeckTest {
    private static final int FULL_DECK_SIZE = 108;
    private static final int JOLLY_COUNT = 4;
    private static final String JOLLY_VALUE = "Jolly";
    private static final String[] SEEDS = {"♠", "♥", "♣", "♦"};
    private static final String[] VALUES = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    private DeckImpl deck;

    @BeforeEach
    void init() {
        this.deck = new DeckImpl();
    }

    @Test
    void testInitialSize() {
        assertEquals(FULL_DECK_SIZE, this.deck.getCards().size());
    }

    @Test
    void testInitialNotEmpty() {
        assertFalse(this.deck.isEmpty());
    }

    @Test
    void testDeckComposition() {
        final List<Card> cards = deck.getCards();

        final long jollyCount = cards.stream()
                .filter(c -> JOLLY_VALUE.equals(c.getValue()))
                .count();
        assertEquals(JOLLY_COUNT, jollyCount);

        for (final String seed : SEEDS) {
            for (final String value : VALUES) {
                final String s = seed;
                final String v = value;
                final long count = cards.stream()
                        .filter(c -> s.equals(c.getSeed()) && v.equals(c.getValue()))
                        .count();
                assertEquals(2, count, "Expected 2 copies of " + value + seed);
            }
        }
    }

    @Test
    void testDrawReducesSize() {
        final Card drawn = this.deck.draw();
        assertNotNull(drawn);
        assertEquals(FULL_DECK_SIZE - 1, this.deck.getCards().size());
    }

    @Test
    void testDrawUntilEmpty() {
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            assertNotNull(this.deck.draw());
        }
        assertTrue(this.deck.isEmpty());
    }

    @Test
    void testDrawOnEmptyReturnsNull() {
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            this.deck.draw();
        }
        assertNull(this.deck.draw());
    }

    @Test
    void testGetCardsIsUnmodifiable() {
        final List<Card> cards = this.deck.getCards();
        assertThrows(UnsupportedOperationException.class, () -> cards.remove(0));
    }

    @Test
    void testResetRestoresFullDeck() {
        this.deck.draw();
        this.deck.draw();
        this.deck.draw();

        this.deck.reset();

        assertEquals(FULL_DECK_SIZE, this.deck.getCards().size());
        assertFalse(this.deck.isEmpty());
    }

    @Test
    void testResetOnEmptyDeck() {
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            this.deck.draw();
        }
        assertTrue(this.deck.isEmpty());

        this.deck.reset();

        assertEquals(FULL_DECK_SIZE, this.deck.getCards().size());
        assertFalse(this.deck.isEmpty());
    }

    @Test
    void testDrawnCardIsRemovedFromDeck() {
        final int initialSize = this.deck.getCards().size();
        final Card drawn = this.deck.draw();
        final List<Card> currentCards = this.deck.getCards();
        
        assertEquals(initialSize - 1, currentCards.size());
        assertFalse(currentCards.stream().anyMatch(c -> c == drawn), 
            "The specific object instance should not be in the deck anymore");
    }
}
