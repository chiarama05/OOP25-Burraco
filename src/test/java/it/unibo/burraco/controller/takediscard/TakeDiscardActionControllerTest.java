package it.unibo.burraco.controller.takediscard;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.discardcard.takediscard.TakeDiscardActionController;
import it.unibo.burraco.controller.discardcard.takediscard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.discardcard.takediscard.TakeDiscardActionView;

class TakeDiscardActionControllerTest {

    private TakeDiscardActionController actionController;
    private TakeDiscardController logicController;
    private Turn turnModel;
    private DiscardPile discardPile;
    private TakeDiscardActionView view;

    @BeforeEach
    void init() {
        logicController = mock(TakeDiscardController.class);
        turnModel = mock(Turn.class);
        discardPile = mock(DiscardPile.class);
        view = mock(TakeDiscardActionView.class);
        actionController = new TakeDiscardActionController(logicController, turnModel, discardPile);
    }

    @Test
    void testHandleSuccess() {
        final DrawResult successResult = mock(DrawResult.class);
        final Player mockPlayer = mock(Player.class);
        when(successResult.getStatus()).thenReturn(DrawResult.Status.SUCCESS_MULTIPLE);
        when(logicController.tryTakeDiscard()).thenReturn(successResult);
        when(turnModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(turnModel.isPlayer1Turn()).thenReturn(true);
        actionController.handle(view);
        verify(view).onTakeDiscardSuccess(eq(mockPlayer), any(), eq(true));
        verify(view, never()).onTakeDiscardError(any());
    }

    @Test
    void testHandleFailure() {
        final DrawResult errorResult = mock(DrawResult.class);
        when(errorResult.getStatus()).thenReturn(DrawResult.Status.ALREADY_DRAWN);
        when(logicController.tryTakeDiscard()).thenReturn(errorResult);
        actionController.handle(view);
        verify(view).onTakeDiscardError(errorResult);
        verify(view, never()).onTakeDiscardSuccess(any(), any(), anyBoolean());
    }
}
