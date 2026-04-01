package it.unibo.burraco.controller.round;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.discardcard.discard.DiscardView;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;

class RoundControllerTest {

    private TableView tableView;
    private ResetManager resetManager;
    private Player p1;
    private Player p2;
    private GameController gameController;
    private InitialDistributionController distController;
    private InitialDistributionView distView;
    private RoundController roundController;

    @BeforeEach
    void setUp() {
        tableView = mock(TableView.class);
        resetManager = mock(ResetManager.class);
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        gameController = mock(GameController.class);
        distController = mock(InitialDistributionController.class);
        distView = mock(InitialDistributionView.class);
        when(gameController.getCommonDeck()).thenReturn(mock(Deck.class));
        when(gameController.getDiscardPile()).thenReturn(mock(DiscardPile.class));
        when(gameController.getDrawManager()).thenReturn(mock(DrawManager.class));
        when(tableView.getDiscardView()).thenReturn(mock(DiscardView.class));
        roundController = new RoundControllerImpl(tableView, resetManager, p1, p2,
                                                 gameController, distController, distView);
    }

    @Test
    void testProcessNewRoundSequence() {
        roundController.processNewRound();
        verify(resetManager).resetRound(any(), any(), any(), any());
        verify(tableView).startNewRound();
        verify(distController).distribute(any(), any(), any(), any());
        verify(distView).refresh(any(), any(), any(), any());
        verify(gameController.getDrawManager()).resetTurn();
        verify(tableView).repaintTable();
    }
}
