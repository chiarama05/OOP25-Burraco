package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.PlayerImpl;

public class PlayerTest {
    private static final String PLAYER_NAME = "Alice";
    private static final String DEFAULT_NAME = "Player";

    private PlayerImpl player;
    private CardImpl aceOfHearts;
    private CardImpl kingOfSpades;
    private CardImpl threeOfClubs;

    @BeforeEach
    void init() {
        this.player = new PlayerImpl(PLAYER_NAME);
        this.aceOfHearts = new CardImpl("♥", "A");
        this.kingOfSpades = new CardImpl("♠", "K");
        this.threeOfClubs = new CardImpl("♣", "3");
    }

    @Test
    void testDefaultName() {
        PlayerImpl defaultPlayer = new PlayerImpl();
        assertEquals(DEFAULT_NAME, defaultPlayer.getName());
    }

    @Test
    void testCustomName() {
        assertEquals(PLAYER_NAME, player.getName());
    }

    @Test
    void testInitialHandIsEmpty() {
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void testAddCardHand() {
        player.addCardHand(aceOfHearts);
        assertEquals(1, player.getHand().size());
        assertTrue(player.hasCard(aceOfHearts));
    }

    @Test
    void testRemoveCardHand() {
        player.addCardHand(aceOfHearts);
        player.removeCardHand(aceOfHearts);
        assertFalse(player.hasCard(aceOfHearts));
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void testRemoveCards() {
        player.addCardHand(aceOfHearts);
        player.addCardHand(kingOfSpades);
        player.addCardHand(threeOfClubs);
        player.removeCards(List.of(aceOfHearts, kingOfSpades));
        assertFalse(player.hasCard(aceOfHearts));
        assertFalse(player.hasCard(kingOfSpades));
        assertTrue(player.hasCard(threeOfClubs));
    }

    @Test
    void testHasFinishedCardsWhenEmpty() {
        assertTrue(player.hasFinishedCards());
    }

    @Test
    void testHasFinishedCardsWhenNotEmpty() {
        player.addCardHand(aceOfHearts);
        assertFalse(player.hasFinishedCards());
    }

    @Test
    void testInitialInPotIsFalse() {
        assertFalse(player.isInPot());
    }

    @Test
    void testSetInPot() {
        player.setInPot(true);
        assertTrue(player.isInPot());
        player.setInPot(false);
        assertFalse(player.isInPot());
    }

    @Test
    void testAddAndGetCombinations() {
        List<Card> comb = List.of(aceOfHearts, kingOfSpades, threeOfClubs);
        player.addCombination(comb);
        assertEquals(1, player.getCombinations().size());
    }

    @Test
    void testAddCombinationIsDefensiveCopy() {
        List<Card> comb = new ArrayList<>(List.of(aceOfHearts, kingOfSpades));
        player.addCombination(comb);
        comb.add(threeOfClubs);
        assertEquals(2, player.getCombinations().get(0).size());
    }

    @Test
    void testGetBurracoCountNone() {
        player.addCombination(List.of(aceOfHearts, kingOfSpades, threeOfClubs));
        assertEquals(0, player.getBurracoCount());
    }

    @Test
    void testGetBurracoCountOne() {
        CardImpl c1 = new CardImpl("♥", "3");
        CardImpl c2 = new CardImpl("♥", "4");
        CardImpl c3 = new CardImpl("♥", "5");
        CardImpl c4 = new CardImpl("♥", "6");
        CardImpl c5 = new CardImpl("♥", "7");
        List<Card> burraco = List.of(aceOfHearts, kingOfSpades, threeOfClubs, c1, c2, c3, c4, c5);
        player.addCombination(burraco);
        assertEquals(1, player.getBurracoCount());
    }

    @Test
    void testDrawPotAddsCardsToHand() {
        player.addToPot(List.of(aceOfHearts, kingOfSpades));
        player.drawPot();
        assertTrue(player.hasCard(aceOfHearts));
        assertTrue(player.hasCard(kingOfSpades));
        assertTrue(player.isInPot());
    }

    @Test
    void testDrawPotClearsPot() {
        player.addToPot(List.of(aceOfHearts));
        player.drawPot();
        assertTrue(player.getPot().isEmpty());
    }

    @Test
    void testDrawPotOnEmptyPotDoesNothing() {
        player.drawPot();
        assertTrue(player.getHand().isEmpty());
        assertFalse(player.isInPot());
    }

    @Test
    void testAddPointsAndGetMatchTotalScore() {
        player.addPointsToMatch(50);
        player.addPointsToMatch(30);
        assertEquals(80, player.getMatchTotalScore());
    }

    @Test
    void testResetForNewRound() {
        player.addCardHand(aceOfHearts);
        player.addToPot(List.of(kingOfSpades));
        player.addCombination(List.of(aceOfHearts, kingOfSpades, threeOfClubs));
        player.setInPot(true);

        player.resetForNewRound();

        assertTrue(player.getHand().isEmpty());
        assertTrue(player.getCombinations().isEmpty());
        assertTrue(player.getPot().isEmpty());
        assertFalse(player.isInPot());
    }
}
