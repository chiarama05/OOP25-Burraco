package it.unibo.burraco.controller.closure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.game.GameNotifier;

class ClosureManagerTest {

    private static final int YEAR = 2005;

    private Turn turnModel;
    private GameNotifier notifier;
    private ScoreController scoreController;
    private ClosureManager manager;
    private Player player;

    @BeforeEach
    void setUp() {
        turnModel = mock(Turn.class);
        notifier = mock(GameNotifier.class);
        scoreController = mock(ScoreController.class);
        player = new PlayerImpl("Player1");
        
        manager = new ClosureManager(turnModel, notifier, YEAR, scoreController);
    }

    @Test
    void testHandleStateAfterDiscardSuccess() {
        player.setInPot(true);
        final List<Card> burraco = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            burraco.add(mock(Card.class));
        }
        player.addCombination(burraco);
        
        assertTrue(player.getHand().isEmpty());

        final boolean result = manager.handleStateAfterDiscard(player);

        assertTrue(result);
        verify(turnModel).setGameFinished(true);
        verify(scoreController).onRoundEnd();
    }

    @Test
    void testHandleStateAfterDiscardFailsNoBurraco() {
        player.setInPot(true);
        assertTrue(player.getHand().isEmpty());

        final boolean result = manager.handleStateAfterDiscard(player);

        assertFalse(result);
        verify(notifier).notifyInvalidClosure();
        verify(turnModel, never()).setGameFinished(true);
    }
}
