package it.unibo.burraco.controller.round;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.DistributionView;
import it.unibo.burraco.view.table.TableView;

class RoundControllerTest {

    private TableView tableView;
    private ResetManager resetManager;
    private Player p1;
    private Player p2;
    private GameModel model;
    private InitialDistributionController distController;
    private DistributionView distView;
    private RoundController roundController;

    @BeforeEach
    void setUp() {
        tableView = mock(TableView.class);
        resetManager = mock(ResetManager.class);
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        model = mock(GameModel.class);
        distController = mock(InitialDistributionController.class);
        distView = mock(DistributionView.class);

        when(model.getCommonDeck()).thenReturn(mock(Deck.class));
        when(model.getDiscardPile()).thenReturn(mock(DiscardPile.class));

        roundController = new RoundControllerImpl(
            tableView, resetManager, p1, p2, model, distController, distView
        );
    }

    @Test
    void testProcessNewRoundSequence() {
        roundController.processNewRound();

        verify(resetManager).resetRound(any(), any(), any(), any());
        verify(model).resetForNewRound();

        verify(tableView).startNewRound();

        verify(distController).distribute(eq(p1), eq(p2), any(), any());

        verify(distView).refresh(any(), any(), any());
        verify(tableView).refreshHandPanel(eq(true), any());
        verify(tableView).repaintTable();
    }
}
