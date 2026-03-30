package it.unibo.burraco.controller.combination;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.combination.putcombination.PutCombinationActionController;
import it.unibo.burraco.controller.combination.putcombination.PutCombinationController;
import it.unibo.burraco.controller.combination.putcombination.PutCombinationResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.combination.PutCombinationView;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifier;

class PutCombinationActionControllerTest {

    private GameController gameController;
    private PutCombinationController putComboController;
    private PutCombinationNotifier notifier;
    private PutCombinationView view;
    private PutCombinationActionController actionController;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        gameController = mock(GameController.class);
        putComboController = mock(PutCombinationController.class);
        notifier = mock(PutCombinationNotifier.class);
        view = mock(PutCombinationView.class);
        mockPlayer = mock(Player.class);
        actionController = new PutCombinationActionController(gameController, putComboController, notifier);
        when(gameController.getCurrentPlayer()).thenReturn(mockPlayer);
    }

    @Test
    void testHandleError() {
        final List<Card> selected = List.of();
        final PutCombinationResult errorResult = mock(PutCombinationResult.class);
        when(errorResult.getStatus()).thenReturn(PutCombinationResult.Status.NO_CARDS_SELECTED);
        when(putComboController.tryPutCombination(selected)).thenReturn(errorResult);
        actionController.handle(selected, view);
        verify(notifier).notifyCombinationError(errorResult);
        verifyNoInteractions(view);
    }

    @Test
    void testHandleSuccessTakePot() {
        final List<Card> selected = List.of(mock(Card.class));
        final PutCombinationResult potResult = mock(PutCombinationResult.class);
        when(potResult.getStatus()).thenReturn(PutCombinationResult.Status.SUCCESS_TAKE_POT);
        when(potResult.getProcessedCombo()).thenReturn(List.of());
        when(potResult.isPlayer1()).thenReturn(true);
        when(putComboController.tryPutCombination(selected)).thenReturn(potResult);

        actionController.handle(selected, view);
        verify(view).onCombinationTakePot(anyList(), eq(true), eq(mockPlayer));
        verifyNoInteractions(notifier);
    }
}