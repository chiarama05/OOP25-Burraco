package it.unibo.burraco.controller.closure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

public class ClosureValidatorTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new PlayerImpl("TestPlayer");
    }

    @Test
    void testEvaluateNormalPlay() {
        player.addCardHand(new CardImpl("♠", "A"));
        assertEquals(ClosureState.OK, ClosureValidator.evaluate(player));
    }

    @Test
    void testEvaluateNoPot() {
        assertTrue(player.getHand().isEmpty());
        assertEquals(ClosureState.ZERO_CARDS_NO_POT, ClosureValidator.evaluate(player));
    }

    @Test
    void testEvaluateNoBurraco() {
        player.setInPot(true);
        assertEquals(ClosureState.ZERO_CARDS_NO_BURRACO, ClosureValidator.evaluate(player));
    }

    @Test
    void testEvaluateCanClose() {
        player.setInPot(true);
        List<Card> burraco = new ArrayList<>();
        for(int i=0; i<7; i++) burraco.add(new CardImpl("♥", String.valueOf(i)));
        player.addCombination(burraco);

        assertEquals(ClosureState.CAN_CLOSE, ClosureValidator.evaluate(player));
    }

    @Test
    void testWouldGetStuckAfterPutCombo() {
        player.setInPot(true);
        player.addCardHand(new CardImpl("♣", "2"));
        player.addCardHand(new CardImpl("♣", "3"));
        
        List<Card> toPlay = List.of(new CardImpl("♣", "2"), new CardImpl("♣", "3"));
        assertTrue(ClosureValidator.wouldGetStuckAfterPutCombo(player, toPlay, 2));
    }
}
