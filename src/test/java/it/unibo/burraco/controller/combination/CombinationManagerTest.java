package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class CombinationManagerTest {
    @Test
    void testPrepareForDisplayMovesJokerToEnd() {
        Card c1 = new CardImpl("♥", "7");
        Card c2 = new CardImpl("♠", "7");
        Card joker = new CardImpl("Jolly", "Jolly");
        
        List<Card> combo1 = new ArrayList<>(List.of(joker, c1, c2));
        List<Card> result1 = CombinationManager.prepareForDisplay(combo1);
        assertEquals(joker, result1.get(2)); 
        
        List<Card> combo2 = new ArrayList<>(List.of(c1, c2, joker));
        List<Card> result2 = CombinationManager.prepareForDisplay(combo2);
        assertEquals(joker, result2.get(2));
    }
}
