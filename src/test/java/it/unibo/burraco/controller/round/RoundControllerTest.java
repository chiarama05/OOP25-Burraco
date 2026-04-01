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
    private InitialDistributionController distController;
    private InitialDistributionView distView;
    private RoundController roundController;

    @BeforeEach
    void setUp() {
        this.tableView = mock(TableView.class);
        this.resetManager = mock(ResetManager.class);
        final Player p1 = mock(Player.class);
        final Player p2 = mock(Player.class);
        final GameController gameController = mock(GameController.class);
        this.distController = mock(InitialDistributionController.class);
        this.distView = mock(InitialDistributionView.class);
        when(gameController.getCommonDeck()).thenReturn(mock(Deck.class));
        when(gameController.getDiscardPile()).thenReturn(mock(DiscardPile.class));
        when(gameController.getDrawManager()).thenReturn(mock(DrawManager.class));
        when(this.tableView.getDiscardView()).thenReturn(mock(DiscardView.class));
        this.roundController = new RoundControllerImpl(this.tableView, this.resetManager, p1, p2,
            gameController, this.distController, this.distView);
    }

    @Test
    void testProcessNewRoundSequence() {
        this.roundController.processNewRound();
        verify(this.resetManager).resetRound(any(), any(), any(), any());
        verify(this.tableView).startNewRound();
        verify(this.distController).distribute(any(), any(), any(), any());
        verify(this.distView).refresh(any(), any(), any(), any());
        verify(this.tableView).repaintTable();
    }
}
