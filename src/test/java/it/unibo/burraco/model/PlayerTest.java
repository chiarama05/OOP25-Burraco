package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;

class PlayerTest {
    private static final String PLAYER_NAME = "Alice";
    private static final String DEFAULT_NAME = "Player";
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";

    private PlayerImpl player;
    private CardImpl aceOfHearts;
    private CardImpl kingOfHearts;
    private CardImpl twoOfSpades;

    @BeforeEach
    void init() {
        this.player = new PlayerImpl(PLAYER_NAME);
        this.aceOfHearts = new CardImpl(HEARTS, "A");
        this.kingOfHearts = new CardImpl(HEARTS, "K");
        this.twoOfSpades = new CardImpl(SPADES, "2");
    }

    @Test
    void testNamedConstructor() {
        assertEquals(PLAYER_NAME, player.getName());
    }

    @Test
    void testDefaultConstructor() {
        final PlayerImpl defaultPlayer = new PlayerImpl();
        assertEquals(DEFAULT_NAME, defaultPlayer.getName());
    }

    @Test
    void testInitialState() {
        assertTrue(player.getHand().isEmpty());
        assertTrue(player.getCombinations().isEmpty());
        assertFalse(player.isInPot());
        assertEquals(0, player.getMatchTotalScore());
        assertTrue(player.hasFinishedCards());
    }

    @Test
    void testAddCardHand() {
        player.addCardHand(aceOfHearts);

        assertEquals(1, player.getHand().size());
        assertTrue(player.hasCard(aceOfHearts));
    }

    @Test
    void testRemoveCards() {
        player.addCardHand(aceOfHearts);
        player.addCardHand(kingOfHearts);
        player.addCardHand(twoOfSpades);

        player.removeCards(List.of(aceOfHearts, kingOfHearts));

        assertEquals(1, player.getHand().size());
        assertFalse(player.hasCard(aceOfHearts));
        assertFalse(player.hasCard(kingOfHearts));
        assertTrue(player.hasCard(twoOfSpades));
    }

    @Test
    void testHasFinishedCardsWhenHandEmpty() {
        player.addCardHand(aceOfHearts);
        assertFalse(player.hasFinishedCards());

        player.removeCardHand(aceOfHearts);
        assertTrue(player.hasFinishedCards());
    }

    @Test
    void testAddToPot() {
        player.addToPot(List.of(aceOfHearts, kingOfHearts));

        assertEquals(2, player.getPot().size());
        assertTrue(player.getPot().contains(aceOfHearts));
        assertTrue(player.getPot().contains(kingOfHearts));
    }

    @Test
    void testAddToPotOverwritesPrevious() {
        player.addToPot(List.of(aceOfHearts));
        player.addToPot(List.of(kingOfHearts, twoOfSpades));

        assertEquals(2, player.getPot().size());
        assertFalse(player.getPot().contains(aceOfHearts));
    }

    @Test
    void testDrawPotAddsCardsToHand() {
        player.addToPot(List.of(aceOfHearts, kingOfHearts));
        player.drawPot();

        assertTrue(player.hasCard(aceOfHearts));
        assertTrue(player.hasCard(kingOfHearts));
        assertEquals(2, player.getHand().size());
    }

    @Test
    void testDrawPotClearsPot() {
        player.addToPot(List.of(aceOfHearts));
        player.drawPot();

        assertTrue(player.getPot().isEmpty());
    }

    @Test
    void testDrawPotSetsInPotTrue() {
        player.addToPot(List.of(aceOfHearts));
        player.drawPot();

        assertTrue(player.isInPot());
    }

    @Test
    void testDrawPotOnEmptyPotDoesNothing() {
        player.drawPot();

        assertTrue(player.getHand().isEmpty());
        assertFalse(player.isInPot());
    }

    @Test
    void testAddCombinationIsDefensiveCopy() {
        final List<Card> comb = new java.util.ArrayList<>(List.of(aceOfHearts, kingOfHearts));
        player.addCombination(comb);
        comb.add(twoOfSpades);

        assertEquals(2, player.getCombinations().get(0).size());
    }

    @Test
    void testAddPointsToMatch() {
        player.addPointsToMatch(10);
        player.addPointsToMatch(25);

        assertEquals(35, player.getMatchTotalScore());
    }

    @Test
    void testResetForNewRound() {
        player.addCardHand(aceOfHearts);
        player.addToPot(List.of(kingOfHearts));
        player.addCombination(List.of(aceOfHearts, kingOfHearts, twoOfSpades));
        player.setInPot(true);
        player.addPointsToMatch(100);

        player.resetForNewRound();

        assertTrue(player.getHand().isEmpty());
        assertTrue(player.getPot().isEmpty());
        assertTrue(player.getCombinations().isEmpty());
        assertFalse(player.isInPot());
        assertEquals(100, player.getMatchTotalScore());
    }
}
