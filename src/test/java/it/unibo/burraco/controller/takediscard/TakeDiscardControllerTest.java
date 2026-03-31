package it.unibo.burraco.controller.takediscard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.discardcard.takediscard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

class TakeDiscardControllerTest {

    private TakeDiscardController controller;
    private DrawManager drawManager;
    private Turn turnModel;
    private DiscardPile discardPile;

    @BeforeEach
    void init() {
        drawManager = mock(DrawManager.class);
        turnModel = mock(Turn.class);
        discardPile = mock(DiscardPile.class);
        controller = new TakeDiscardController(drawManager, turnModel, discardPile);
    }

    @Test
    void testTryTakeDiscardCallsManagerWithCorrectParams() {
        final Player mockPlayer = mock(Player.class);
        final List<Card> mockCards = List.of(mock(Card.class));
        final DrawResult mockResult = mock(DrawResult.class);
        when(turnModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(discardPile.getCards()).thenReturn(mockCards);
        when(drawManager.drawFromDiscard(mockPlayer, mockCards)).thenReturn(mockResult);
        final DrawResult result = controller.tryTakeDiscard();
        assertEquals(mockResult, result);
        verify(drawManager).drawFromDiscard(mockPlayer, mockCards);
    }
}
