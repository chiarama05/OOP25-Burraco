package it.unibo.burraco.controller.closure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.game.GameNotifier;

class ClosureManagerTest {
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
        
        manager = new ClosureManager(turnModel, notifier, 2005, scoreController);
    }

    @Test
    void testHandleStateAfterDiscard_Success() {
        player.setInPot(true);
        List<Card> burraco = new ArrayList<>();
        for(int i=0; i<7; i++) burraco.add(mock(Card.class));
        player.addCombination(burraco);
        
        assertTrue(player.getHand().isEmpty());

        boolean result = manager.handleStateAfterDiscard(player);

        assertTrue(result);
        verify(turnModel).setGameFinished(true);
        verify(scoreController).onRoundEnd();
    }

    @Test
    void testHandleStateAfterDiscard_FailsNoBurraco() {
        player.setInPot(true);
        assertTrue(player.getHand().isEmpty());

        boolean result = manager.handleStateAfterDiscard(player);

        assertFalse(result);
        verify(notifier).notifyInvalidClosure();
        verify(turnModel, never()).setGameFinished(true);
    }
}
