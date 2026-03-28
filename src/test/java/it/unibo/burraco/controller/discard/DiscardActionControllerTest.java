package it.unibo.burraco.controller.discard;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import it.unibo.burraco.controller.discardcard.discard.*;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.discardcard.discard.DiscardActionView;

public class DiscardActionControllerTest {

    @Test
    void testHandleSuccessInformsView() {
        DiscardActionView view = mock(DiscardActionView.class);
        DiscardController logic = mock(DiscardController.class);
        DiscardResult successResult = mock(DiscardResult.class);

        when(successResult.isValid()).thenReturn(true);
        when(successResult.getCurrentPlayer()).thenReturn(mock(Player.class));
        when(logic.tryDiscard(any())).thenReturn(successResult);

        DiscardActionController actionCtrl = new DiscardActionController(logic);
        actionCtrl.handle(view, true);

        verify(view).onDiscardSuccess(any(), any(), eq(true));
    }

    @Test
    void testHandleErrorInformsView() {
        DiscardActionView view = mock(DiscardActionView.class);
        DiscardController logic = mock(DiscardController.class);
        DiscardResult errorResult = mock(DiscardResult.class);

        when(errorResult.isValid()).thenReturn(false);
        when(errorResult.getMessage()).thenReturn("error_msg");
        when(logic.tryDiscard(any())).thenReturn(errorResult);

        DiscardActionController actionCtrl = new DiscardActionController(logic);
        actionCtrl.handle(view, true);

        verify(view).onDiscardError("error_msg");
    }
}