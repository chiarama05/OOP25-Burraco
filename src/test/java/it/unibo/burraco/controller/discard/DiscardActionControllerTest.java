package it.unibo.burraco.controller.discard;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.discardcard.discard.DiscardActionController;
import it.unibo.burraco.controller.discardcard.discard.DiscardController;
import it.unibo.burraco.controller.discardcard.discard.DiscardResult;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.discardcard.discard.DiscardActionView;

class DiscardActionControllerTest {

    @Test
    void testHandleSuccessInformsView() {
        final DiscardActionView view = mock(DiscardActionView.class);
        final DiscardController logic = mock(DiscardController.class);
        final DiscardResult successResult = mock(DiscardResult.class);

        when(successResult.isValid()).thenReturn(true);
        when(successResult.getCurrentPlayer()).thenReturn(mock(Player.class));
        when(logic.tryDiscard(any())).thenReturn(successResult);

        final DiscardActionController actionCtrl = new DiscardActionController(logic);
        actionCtrl.handle(view, true);

        verify(view).onDiscardSuccess(any(), any(), eq(true));
    }

    @Test
    void testHandleErrorInformsView() {
        final DiscardActionView view = mock(DiscardActionView.class);
        final DiscardController logic = mock(DiscardController.class);
        final DiscardResult errorResult = mock(DiscardResult.class);

        final String errorMsg = "error_msg";
        when(errorResult.isValid()).thenReturn(false);
        when(errorResult.getMessage()).thenReturn(errorMsg);
        when(logic.tryDiscard(any())).thenReturn(errorResult);

        final DiscardActionController actionCtrl = new DiscardActionController(logic);
        actionCtrl.handle(view, true);

        verify(view).onDiscardError(errorMsg);
    }
}
