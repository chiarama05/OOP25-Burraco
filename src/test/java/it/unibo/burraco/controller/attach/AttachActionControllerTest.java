package it.unibo.burraco.controller.attach;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.attach.AttachView;
import it.unibo.burraco.view.notification.attach.AttachNotifier;

/**
 * Test class for AttachActionController.
 */
class AttachActionControllerTest {

    private AttachController attachController;
    private PotManager potManager;
    private ClosureManager closureManager;
    private AttachNotifier attachNotifier;
    private AttachView view;
    private Player player;
    private SoundController soundController;

    private List<Card> selectedCards;
    private List<Card> combinationCards;

    private AttachActionController controller;

    @BeforeEach
    void setUp() {
        final GameController gameController = mock(GameController.class); // aggiunto final/tipo
        attachController = mock(AttachController.class);
        potManager = mock(PotManager.class);
        closureManager = mock(ClosureManager.class);
        attachNotifier = mock(AttachNotifier.class);
        view = mock(AttachView.class);
        player = mock(Player.class);
        final DrawManager drawManager = mock(DrawManager.class); // aggiunto final/tipo
        soundController = mock(SoundController.class);

        selectedCards = List.of(mock(Card.class));
        combinationCards = List.of(mock(Card.class));

        when(gameController.getModel().getCurrentPlayer()).thenReturn(player);
        when(gameController.getDrawManager()).thenReturn(drawManager);
        when(gameController.getAttachController()).thenReturn(attachController);
        when(gameController.getSoundController()).thenReturn(soundController);
        when(gameController.getModel().isPlayer1(player)).thenReturn(true);
        when(drawManager.hasDrawn()).thenReturn(true);
        controller = new AttachActionController(
                gameController, potManager, closureManager, attachNotifier, true);
    }

    @Test
    void whenNotDrawnthenNotifierCalled() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.NOT_DRAWN);

        controller.handle(selectedCards, combinationCards, view);

        verify(attachNotifier).notifyAttachError(AttachResult.NOT_DRAWN);
        verify(view, never()).updateCombinationVisuals();
    }

    @Test
    void whenWrongPlayerthenNotifierCalled() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.WRONG_PLAYER);

        controller.handle(selectedCards, combinationCards, view);

        verify(attachNotifier).notifyAttachError(AttachResult.WRONG_PLAYER);
        verify(view, never()).updateCombinationVisuals();
    }

    @Test
    void whenInvalidCombinationthenNotifierCalled() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.INVALID_COMBINATION);

        controller.handle(selectedCards, combinationCards, view);

        verify(attachNotifier).notifyAttachError(AttachResult.INVALID_COMBINATION);
        verify(view, never()).updateCombinationVisuals();
    }

    @Test
    void whenSuccessthenViewUpdated() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.SUCCESS);

        controller.handle(selectedCards, combinationCards, view);

        verify(view).updateCombinationVisuals();
        verify(view).onAttachSuccess(player, true);
        verify(attachNotifier, never()).notifyAttachError(any());
    }

    @Test
    void whenSuccessBurracothenSoundPlayedAndViewUpdated() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.SUCCESS_BURRACO);

        controller.handle(selectedCards, combinationCards, view);

        verify(soundController).playBurracoSound();
        verify(view).updateCombinationVisuals();
        verify(view).onAttachSuccess(player, true);
    }

    @Test
    void whenSuccessTakePotthenPotHandledAndViewUpdated() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.SUCCESS_TAKE_POT);

        controller.handle(selectedCards, combinationCards, view);

        verify(view).updateCombinationVisuals();
        verify(potManager).handlePot(false);
        verify(view).onAttachTakePot(player, true);
    }

    @Test
    void whenSuccessClosethenClosureHandledAndViewUpdated() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.SUCCESS_CLOSE);

        controller.handle(selectedCards, combinationCards, view);

        verify(view).updateCombinationVisuals();
        verify(closureManager).handleStateAfterAction(player);
        verify(view).onAttachClose(player, true);
    }

    @Test
    void whenSuccessStuckthenClosureHandledAndViewUpdated() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.SUCCESS_STUCK);

        controller.handle(selectedCards, combinationCards, view);

        verify(view).updateCombinationVisuals();
        verify(closureManager).handleStateAfterAction(player);
        verify(view).onAttachClose(player, true);
    }

    @Test
    void whenWouldGetStuckthenNotifierCalled() {
        when(attachController.tryAttach(any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(AttachResult.WOULD_GET_STUCK);

        controller.handle(selectedCards, combinationCards, view);

        verify(attachNotifier).notifyAttachError(AttachResult.WOULD_GET_STUCK);
        verify(view, never()).updateCombinationVisuals();
    }
}
