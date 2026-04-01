package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.combination.set.SetAttachUtils;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

class SetAttachUtilsTest {

    @Test
    void testCanAttachSameValue() {
        final List<Card> set = new ArrayList<>(List.of(
            new CardImpl("♥", "10"),
            new CardImpl("♠", "10"),
            new CardImpl("♦", "10")
        ));
        final Card newTen = new CardImpl("♣", "10");
        assertTrue(SetAttachUtils.canAttachToSet(set, newTen));
    }

    @Test
    void testCannotAttachSecondWildcard() {
        final List<Card> set = new ArrayList<>(List.of(
            new CardImpl("♥", "10"),
            new CardImpl("♠", "10"),
            new CardImpl("Jolly", "Jolly")
        ));
        final Card two = new CardImpl("♦", "2");
        assertFalse(SetAttachUtils.canAttachToSet(set, two));
    }
}
