package it.unibo.burraco.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.distributioncard.DistributionManager;
import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

/*Table Creation: hands distribution, pots, and initial hand*/ 
public class TableCreationTest {

    private static final int HAND_SIZE = 11;
    private static final int DECK_AFTER_DIST = 108 - (HAND_SIZE * 2) - (HAND_SIZE * 2) - 1;

    private Player p1, p2;
    private Deck deck;
    private DiscardPile discardPile;
    private DistributionManager dist;

    @BeforeEach
    void setUp() {
        p1 = new PlayerImpl("Alice");
        p2= new PlayerImpl("Bob");
        deck= new DeckImpl();
        discardPile= new DiscardPileImpl();
        dist= new DistributionManagerImpl();
    }

    @Test
    void testDeckInitialSize() {
        assertEquals(108, deck.getCards().size());
    }

    @Test
    void testDeckNotEmptyAfterDistribution() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertFalse(deck.isEmpty());
    }

    @Test
    void testDeckSizeAfterDistribution() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertEquals(DECK_AFTER_DIST, deck.getCards().size());
    }

    @Test
    void testDeckDrawReturnsCard() {
        Card c = deck.draw();
        assertNotNull(c);
        assertEquals(107, deck.getCards().size());
    }

    @Test
    void testDeckReset() {
        deck.draw(); deck.draw();
        deck.reset();
        assertEquals(108, deck.getCards().size());
    }

    @Test
    void testPlayer1ReceivesElevenCards() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertEquals(HAND_SIZE, p1.getHand().size());
    }

    @Test
    void testPlayer2ReceivesElevenCards() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertEquals(HAND_SIZE, p2.getHand().size());
    }

    @Test
    void testHandCardsNotNull() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        p1.getHand().forEach(c -> assertNotNull(c));
        p2.getHand().forEach(c -> assertNotNull(c));
    }

    @Test
    void testAfterDrawingPotHandIs22() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        p1.drawPot();
        assertEquals(HAND_SIZE * 2, p1.getHand().size());
    }

    @Test
    void testPlayersNotInPotAtStart() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertFalse(p1.isInPot());
        assertFalse(p2.isInPot());
    }

    @Test
    void testDiscardPileHasOneCard() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertEquals(1, discardPile.getCards().size());
    }

    @Test
    void testDiscardPileCardNotNull() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertNotNull(discardPile.getCards().get(0));
    }

    @Test
    void testDiscardPileAddAndDrawLast() {
        Card c = deck.draw();
        discardPile.add(c);
        assertEquals(c, discardPile.drawLast());
        assertTrue(discardPile.isEmpty());
    }

    @Test
    void testDiscardPileReset() {
        discardPile.add(deck.draw());
        discardPile.reset();
        assertTrue(discardPile.isEmpty());
    }

    @Test
    void testNoCombinationsAtStart() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        assertTrue(p1.getCombinations().isEmpty());
        assertTrue(p2.getCombinations().isEmpty());
    }

    @Test
    void testDistributionRepeatableAfterReset() {
        dist.distributeInitialCards(p1, p2, deck, discardPile);
        int firstDeckSize = deck.getCards().size();

        p1.resetForNewRound(); p2.resetForNewRound();
        deck.reset(); discardPile.reset();
        dist.distributeInitialCards(p1, p2, deck, discardPile);

        assertEquals(firstDeckSize, deck.getCards().size());
        assertEquals(HAND_SIZE, p1.getHand().size());
    }
}



