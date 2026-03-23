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
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.deck.DeckImpl;

public class DeckTest {
    private static final int FULL_DECK_SIZE = 108;
    private static final int SEEDS_COUNT = 4;
    private static final int VALUES_COUNT = 13;
    private static final int JOLLY_COUNT = 4;

    private DeckImpl deck;

    @BeforeEach
    void init() {
        this.deck = new DeckImpl();
    }

    @Test
    void testInitialSize() {
        assertEquals(FULL_DECK_SIZE, deck.getCards().size());
    }

    @Test
    void testInitialStateNotEmpty() {
        assertFalse(deck.isEmpty());
    }

    @Test
    void testDeckContainsCorrectCards() {
        List<Card> cards = deck.getCards();

        long jollyCount = cards.stream()
            .filter(c -> c.getValue().equals("Jolly"))
            .count();
        assertEquals(JOLLY_COUNT, jollyCount);

        long normalCount = cards.stream()
            .filter(c -> !c.getValue().equals("Jolly"))
            .count();
        assertEquals(SEEDS_COUNT * VALUES_COUNT * 2, normalCount);
    }

    @Test
    void testDrawReducesSize() {
        Card drawn = deck.draw();
        assertNotNull(drawn);
        assertEquals(FULL_DECK_SIZE - 1, deck.getCards().size());
    }

    @Test
    void testDrawUntilEmpty() {
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            assertNotNull(deck.draw());
        }
        assertTrue(deck.isEmpty());
    }

    @Test
    void testDrawOnEmptyReturnsNull() {
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            deck.draw();
        }
        assertNull(deck.draw());
    }

    @Test
    void testGetCardsIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            deck.getCards().add(new CardImpl("♠", "A"));
        });
    }

    @Test
    void testResetRestoresFullDeck() {
        for (int i = 0; i < 10; i++) {
            deck.draw();
        }
        deck.reset();
        assertEquals(FULL_DECK_SIZE, deck.getCards().size());
        assertFalse(deck.isEmpty());
    }

    @Test
    void testResetOnEmptyDeck() {
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            deck.draw();
        }
        deck.reset();
        assertEquals(FULL_DECK_SIZE, deck.getCards().size());
        assertFalse(deck.isEmpty());
    }
}
