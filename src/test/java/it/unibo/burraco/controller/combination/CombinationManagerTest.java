package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class CombinationManagerTest {

    @Test
    void testPrepareForDisplayMovesJollyToEnd() {
        final Card c1 = new CardImpl("♥", "7");
        final Card c2 = new CardImpl("♠", "7");
        final Card jolly = new CardImpl("Jolly", "Jolly");

        final List<Card> combo1 = new ArrayList<>(List.of(jolly, c1, c2));
        final List<Card> result1 = CombinationManager.prepareForDisplay(combo1);
        assertEquals(jolly, result1.get(2));

        final List<Card> combo2 = new ArrayList<>(List.of(c1, c2, jolly));
        final List<Card> result2 = CombinationManager.prepareForDisplay(combo2);
        assertEquals(jolly, result2.get(2));
    }
}
