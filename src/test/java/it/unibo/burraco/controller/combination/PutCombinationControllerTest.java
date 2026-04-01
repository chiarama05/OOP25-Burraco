package it.unibo.burraco.controller.combination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.combination.putcombination.PutCombinationController;
import it.unibo.burraco.controller.combination.putcombination.PutCombinationResult;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;

class PutCombinationControllerTest {

    private static final String FIVE = "5";
    private static final String HEARTS = "♥";
    private static final String SPADES = "♠";
    private static final String DIAMONDS = "♦";

    private PutCombinationController controller;
    private DrawManager drawManager;
    private Player player;

    @BeforeEach
    void init() {
        final GameController gameController = mock(GameController.class, RETURNS_DEEP_STUBS);
        final Turn turn = mock(Turn.class);
        this.drawManager = mock(DrawManager.class);
        this.player = spy(new PlayerImpl("TestPlayer"));
        when(turn.getCurrentPlayer()).thenReturn(this.player);

        this.controller = new PutCombinationController(
            gameController,
            this.drawManager,
            mock(PotManager.class),
            mock(ClosureManager.class),
            turn
        );
    }

    @Test
    void testErrorIfNotDrawn() {
        when(this.drawManager.hasDrawn()).thenReturn(false);
        final PutCombinationResult result = this.controller.tryPutCombination(List.of(mock(Card.class)));
        assertEquals(PutCombinationResult.Status.NOT_DRAWN, result.getStatus());
    }

    @Test
    void testSuccessTakePot() {
        when(this.drawManager.hasDrawn()).thenReturn(true);
        final Card c1 = new CardImpl(HEARTS, FIVE);
        final Card c2 = new CardImpl(SPADES, FIVE);
        final Card c3 = new CardImpl(DIAMONDS, FIVE);
        this.player.addCardHand(c1);
        this.player.addCardHand(c2);
        this.player.addCardHand(c3);
        final List<Card> cardsToPut = List.of(c1, c2, c3);
        this.player.setInPot(false);
        final PutCombinationResult result = this.controller.tryPutCombination(cardsToPut);
        assertEquals(PutCombinationResult.Status.SUCCESS_TAKE_POT, result.getStatus());
    }
}
