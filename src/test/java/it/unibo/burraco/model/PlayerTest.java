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
    void testConstructors() {
        assertEquals(PLAYER_NAME, this.player.getName());
        final PlayerImpl defaultPlayer = new PlayerImpl();
        assertEquals(DEFAULT_NAME, defaultPlayer.getName());
    }

    @Test
    void testInitialState() {
        assertTrue(this.player.getHand().isEmpty());
        assertTrue(this.player.getCombinations().isEmpty());
        assertFalse(this.player.isInPot());
        assertEquals(0, this.player.getMatchTotalScore());
        assertTrue(this.player.hasFinishedCards());
    }

    @Test
    void testHandManagement() {
        this.player.addCardHand(this.aceOfHearts);
        assertEquals(1, this.player.getHand().size());
        assertTrue(this.player.hasCard(this.aceOfHearts));

        this.player.addCardHand(this.kingOfHearts);
        this.player.removeCards(List.of(this.aceOfHearts));
        assertFalse(this.player.hasCard(this.aceOfHearts));
        assertTrue(this.player.hasCard(this.kingOfHearts));
    }

    @Test
    void testPotInteraction() {
        final List<Card> potCards = List.of(this.aceOfHearts, this.kingOfHearts);
        this.player.addToPot(potCards);
        
        assertEquals(2, this.player.getPot().size());
        this.player.drawPot();

        assertTrue(this.player.getPot().isEmpty());
        assertTrue(this.player.isInPot());
        assertEquals(2, this.player.getHand().size());
    }

    @Test
    void testAddCombinationIsDefensiveCopy() {
        final List<Card> comb = new ArrayList<>(List.of(this.aceOfHearts, this.kingOfHearts));
        this.player.addCombination(comb);

        comb.add(this.twoOfSpades);
        assertEquals(2, this.player.getCombinations().get(0).size());
    }

    @Test
    void testScorePersistence() {
        this.player.addPointsToMatch(10);
        this.player.addPointsToMatch(25);
        assertEquals(35, this.player.getMatchTotalScore());
    }

    @Test
    void testResetForNewRound() {
        this.player.addCardHand(this.aceOfHearts);
        this.player.setInPot(true);
        this.player.addPointsToMatch(100);

        this.player.resetForNewRound();

        assertTrue(this.player.getHand().isEmpty());
        assertFalse(this.player.isInPot());
        assertEquals(100, this.player.getMatchTotalScore());
    }
}
