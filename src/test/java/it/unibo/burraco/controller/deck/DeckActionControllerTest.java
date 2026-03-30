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
        gameController = mock(GameController.class);
        drawManager = mock(DrawManager.class);
        notifier = mock(DeckNotifier.class);
        view = mock(DeckDrawView.class);
        player = mock(Player.class);
        deck = mock(DeckImpl.class);
        when(gameController.getCurrentPlayer()).thenReturn(player);
        when(gameController.getCommonDeck()).thenReturn(deck);
        when(player.getHand()).thenReturn(List.of(mock(Card.class)));
        controller = new DeckActionController(gameController, drawManager, notifier);
    }

    @Test
    void whenDrawSucceeds_thenViewIsNotified() {
        DrawResult success = DrawResult.success(mock(Card.class));
        when(drawManager.drawFromDeck(player, deck)).thenReturn(success);
        controller.handle(view);
        verify(view).onDrawSuccess(player, player.getHand());
        verify(notifier, never()).notifyDrawError(any());
    }

    @Test
    void whenDrawFails_thenNotifierIsCalledAndViewIsNot() {
        DrawResult failure = DrawResult.alreadyDrawn(); 
        when(drawManager.drawFromDeck(player, deck)).thenReturn(failure);
        controller.handle(view);
        verify(notifier).notifyDrawError(failure);
        verify(view, never()).onDrawSuccess(any(), any());
    }

    @Test
    void handle_alwaysGetsCurrentPlayerAndDeckFromController() {
        when(drawManager.drawFromDeck(any(), any()))
            .thenReturn(DrawResult.success(mock(Card.class))); 
        controller.handle(view);
        verify(gameController).getCurrentPlayer();
        verify(gameController).getCommonDeck();
    }
}

