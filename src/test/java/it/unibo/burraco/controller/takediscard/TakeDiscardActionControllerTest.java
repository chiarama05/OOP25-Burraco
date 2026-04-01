package it.unibo.burraco.controller.takediscard;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
    private TakeDiscardActionView view;

    @BeforeEach
    void init() {
        this.logicController = mock(TakeDiscardController.class);
        this.turnModel = mock(Turn.class);
        final DiscardPile discardPile = mock(DiscardPile.class);
        this.view = mock(TakeDiscardActionView.class);
        this.actionController = new TakeDiscardActionController(this.logicController, this.turnModel, discardPile);
    }

    @Test
    void testHandleSuccessInformsView() {
        final DrawResult successResult = mock(DrawResult.class);
        final Player mockPlayer = mock(Player.class);

        when(successResult.getStatus()).thenReturn(DrawResult.Status.SUCCESS_MULTIPLE);
        when(this.logicController.tryTakeDiscard()).thenReturn(successResult);
        when(this.turnModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(this.turnModel.isPlayer1Turn()).thenReturn(true);
        when(mockPlayer.getHand()).thenReturn(List.of());

        this.actionController.handle(this.view);

        verify(this.view).onTakeDiscardSuccess(eq(mockPlayer), any(), eq(true));
        verify(this.view, never()).onTakeDiscardError(any());
    }

    @Test
    void testHandleFailureInformsViewOfError() {
        final DrawResult errorResult = mock(DrawResult.class);

        when(errorResult.getStatus()).thenReturn(DrawResult.Status.ALREADY_DRAWN);
        when(this.logicController.tryTakeDiscard()).thenReturn(errorResult);

        this.actionController.handle(this.view);

        verify(this.view).onTakeDiscardError(errorResult);
        verify(this.view, never()).onTakeDiscardSuccess(any(), any(), anyBoolean());
    }
}
