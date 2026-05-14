package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.Move.Type;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;

class GameModelTest {

    private GameModel game;
    private static final String P1_NAME = "Alice";
    private static final String P2_NAME = "Bob";

    @BeforeEach
    void setUp() {
        game = new GameModelImpl(P1_NAME, P2_NAME);
    }

    @Test
    void testInitialState() {
        assertNotNull(game.getCurrentPlayer());
        assertEquals(P1_NAME, game.getPlayer1().getName());
        assertEquals(P2_NAME, game.getPlayer2().getName());
        assertFalse(game.isGameOver());
        assertFalse(game.hasDrawn(), "All'inizio del turno non si deve aver ancora pescato.");
    }

    @Test
    void testTurnTransition() {
        Player first = game.getCurrentPlayer();
        game.nextTurn();
        Player second = game.getCurrentPlayer();
        
        assertNotEquals(first, second, "Il giocatore deve cambiare dopo nextTurn().");
    }

    @Test
    void testDrawFromDeck() {
        Move drawMove = new Move(Type.DRAW_DECK, Collections.emptyList(), Collections.emptyList());
        
        assertTrue(game.validateMove(drawMove).isValid());
        
        int handSizeBefore = game.getCurrentPlayer().getHand().size();
        game.applyMove(drawMove);
        
        assertTrue(game.hasDrawn());
        assertEquals(handSizeBefore + 1, game.getCurrentPlayer().getHand().size());
    }

    @Test
    void testCannotDrawTwice() {
        Move drawMove = new Move(Type.DRAW_DECK, Collections.emptyList(), Collections.emptyList());
        game.applyMove(drawMove);
        
        MoveResult secondDraw = game.validateMove(drawMove);
        assertFalse(secondDraw.isValid());
        assertEquals(MoveResult.Status.ALREADY_DRAWN, secondDraw.getStatus());
    }

    @Test
    void testDiscard() {
        game.applyMove(new Move(Type.DRAW_DECK, Collections.emptyList(), Collections.emptyList()));
        
        Player current = game.getCurrentPlayer();
        Card toDiscard = current.getHand().get(0);
        
        Move discardMove = new Move(Type.DISCARD, List.of(toDiscard), Collections.emptyList());
        
        MoveResult result = game.applyMove(discardMove);
        assertTrue(result.isValid());
        assertFalse(current.getHand().contains(toDiscard), "La carta deve essere stata rimossa dalla mano.");
    }

    @Test
    void testPutCombinationValidation() {
        game.applyMove(new Move(Type.DRAW_DECK, Collections.emptyList(), Collections.emptyList()));
        
        Move emptyCombo = new Move(Type.PUT_COMBINATION, Collections.emptyList(), Collections.emptyList());
        MoveResult result = game.validateMove(emptyCombo);
        
        assertFalse(result.isValid());
        assertEquals(MoveResult.Status.NO_CARDS_SELECTED, result.getStatus());
    }

    @Test
    void testResetRound() {
        game.applyMove(new Move(Type.DRAW_DECK, Collections.emptyList(), Collections.emptyList()));
        game.resetForNewRound();
        
        assertFalse(game.hasDrawn());
        assertNull(game.getWinner());
    }
}
