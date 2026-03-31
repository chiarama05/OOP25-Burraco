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
        this.drawManager = mock(DrawManager.class);
        this.turnModel = mock(Turn.class);
        this.discardPile = mock(DiscardPile.class);
        this.controller = new TakeDiscardController(this.drawManager, this.turnModel, this.discardPile);
    }

    @Test
    void testTryTakeDiscardCallsManagerWithCorrectParams() {
        final Player mockPlayer = mock(Player.class);
        final List<Card> mockCards = List.of(mock(Card.class));
        final DrawResult mockResult = mock(DrawResult.class);

        when(this.turnModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(this.discardPile.getCards()).thenReturn(mockCards);
        when(this.drawManager.drawFromDiscard(mockPlayer, mockCards)).thenReturn(mockResult);

        final DrawResult result = this.controller.tryTakeDiscard();

        assertEquals(mockResult, result);
        verify(this.drawManager).drawFromDiscard(mockPlayer, mockCards);
        verify(this.turnModel).getCurrentPlayer();
        verify(this.discardPile).getCards();
    }
}