package it.unibo.burraco.controller.deck;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.deck.DeckDrawView;
import it.unibo.burraco.view.notification.deck.DeckNotifier;

class DeckActionControllerTest {

    private GameController gameController;
    private DrawManager drawManager;
    private DeckNotifier notifier;
    private DeckDrawView view;
    private Player player;
    private DeckImpl deck;
    private DeckActionController controller;

    @BeforeEach
    void setUp() {
        this.gameController = mock(GameController.class);
        this.drawManager = mock(DrawManager.class);
        this.notifier = mock(DeckNotifier.class);
        this.view = mock(DeckDrawView.class);
        this.player = mock(Player.class);
        this.deck = mock(DeckImpl.class);

        when(this.gameController.getModel().getCurrentPlayer()).thenReturn(this.player);
        when(this.gameController.getModel().getCommonDeck()).thenReturn(this.deck);
        when(this.player.getHand()).thenReturn(List.of(mock(Card.class)));

        this.controller = new DeckActionController(this.gameController, this.drawManager, this.notifier);
    }

    @Test
    void testHandleWhenDrawSucceedsThenViewIsNotified() {
        final DrawResult success = new DrawResult(mock(Card.class));
        when(this.drawManager.drawFromDeck(this.player, this.deck)).thenReturn(success);

        this.controller.handle(this.view);

        verify(this.view).onDrawSuccess(this.player, this.player.getHand());
        verify(this.notifier, never()).notifyDrawError(any());
    }

    @Test
    void testHandleWhenDrawFailsThenNotifierIsCalled() {
        final DrawResult failure = new DrawResult(DrawResult.Status.ALREADY_DRAWN);
        when(this.drawManager.drawFromDeck(this.player, this.deck)).thenReturn(failure);

        this.controller.handle(this.view);

        verify(this.notifier).notifyDrawError(failure);
        verify(this.view, never()).onDrawSuccess(any(), any());
    }

    @Test
    void testHandleAlwaysGetsDataFromGameController() {
        final DrawResult dummyResult = new DrawResult(mock(Card.class));
        when(this.drawManager.drawFromDeck(any(), any())).thenReturn(dummyResult);

        this.controller.handle(this.view);

        verify(this.gameController).getModel().getCurrentPlayer();
        verify(this.gameController).getModel().getCommonDeck();
        verify(this.drawManager).drawFromDeck(this.player, this.deck);
    }
}
