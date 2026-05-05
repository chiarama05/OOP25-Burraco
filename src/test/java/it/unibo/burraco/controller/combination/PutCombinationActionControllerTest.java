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

    private PutCombinationController putComboController;
    private PutCombinationNotifier notifier;
    private PutCombinationView view;
    private PutCombinationActionController actionController;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        final GameController gameController = mock(GameController.class);
        this.putComboController = mock(PutCombinationController.class);
        this.notifier = mock(PutCombinationNotifier.class);
        this.view = mock(PutCombinationView.class);
        this.mockPlayer = mock(Player.class);
        this.actionController = new PutCombinationActionController(gameController, this.putComboController, this.notifier);
        when(gameController.getModel().getCurrentPlayer()).thenReturn(this.mockPlayer);
    }

    @Test
    void testHandleError() {
        final List<Card> selected = List.of();
        final PutCombinationResult errorResult = mock(PutCombinationResult.class);
        when(errorResult.getStatus()).thenReturn(PutCombinationResult.Status.NO_CARDS_SELECTED);
        when(this.putComboController.tryPutCombination(selected)).thenReturn(errorResult);
        this.actionController.handle(selected, this.view);
        verify(this.notifier).notifyCombinationError(errorResult);
        verifyNoInteractions(this.view);
    }

    @Test
    void testHandleSuccessTakePot() {
        final List<Card> selected = List.of(mock(Card.class));
        final PutCombinationResult potResult = mock(PutCombinationResult.class);
        when(potResult.getStatus()).thenReturn(PutCombinationResult.Status.SUCCESS_TAKE_POT);
        when(potResult.getProcessedCombo()).thenReturn(List.of());
        when(potResult.isPlayer1()).thenReturn(true);
        when(this.putComboController.tryPutCombination(selected)).thenReturn(potResult);

        this.actionController.handle(selected, this.view);
        verify(this.view).onCombinationTakePot(anyList(), eq(true), eq(this.mockPlayer));
        verifyNoInteractions(this.notifier);
    }
}
