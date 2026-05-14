package it.unibo.burraco.model.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardImpl;

class MoveTest {

    private final Card testCard = new CardImpl("♥", "A");

    @Test
    void testMoveConstructionAndImmutability() {
        final List<Card> cards = new ArrayList<>(List.of(testCard));
        final Move move = new Move(Move.Type.DISCARD, cards);

        assertEquals(Move.Type.DISCARD, move.getType());
        assertEquals(1, move.getSelectedCards().size());
        assertTrue(move.getTargetCombination().isEmpty());

        assertThrows(UnsupportedOperationException.class, () -> move.getSelectedCards().add(testCard));
    }

    @Test
    void testMoveWithTarget() {
        final List<Card> selected = List.of(testCard);
        final List<Card> target = List.of(new CardImpl("♠", "2"));
        final Move move = new Move(Move.Type.ATTACH, selected, target);

        assertEquals(1, move.getTargetCombination().size());
        assertEquals("2♠", move.getTargetCombination().get(0).toString());
    }

    @Test
    void testMoveResultLogic() {
        assertTrue(MoveResult.ok().isValid());
        assertTrue(MoveResult.success(MoveResult.Status.SUCCESS_BURRACO, List.of(), true).isValid());
        assertTrue(MoveResult.success(MoveResult.Status.ROUND_WON, List.of(), false).isValid());

        assertFalse(MoveResult.error(MoveResult.Status.INVALID_MOVE).isValid());
        assertFalse(MoveResult.error(MoveResult.Status.WRONG_PLAYER).isValid());
    }

    @Test
    void testMoveResultProperties() {
        final List<Card> cards = List.of(testCard);
        final MoveResult res = MoveResult.success(MoveResult.Status.SUCCESS, cards, true);

        assertEquals(MoveResult.Status.SUCCESS, res.getStatus());
        assertTrue(res.isPlayer1());
        assertEquals(1, res.getProcessedCards().size());

        assertThrows(UnsupportedOperationException.class, () -> res.getProcessedCards().add(testCard));
    }
}
