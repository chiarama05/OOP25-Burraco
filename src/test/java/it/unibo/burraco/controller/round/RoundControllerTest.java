package it.unibo.burraco.controller.round;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.view.table.BurracoView;
import it.unibo.burraco.view.table.DistributionView;
import java.util.Collections;

class RoundControllerTest {

    private GameModel model;
    private BurracoView tableView;
    private ResetManager resetManager;
    private InitialDistributionController distController;
    private DistributionView distView;
    private Player p1;
    private Player p2;
    private RoundController controller;

    @BeforeEach
    void setUp() {
        this.tableView = mock(BurracoView.class);
        this.resetManager = mock(ResetManager.class);
        this.model = mock(GameModel.class);
        this.distController = mock(InitialDistributionController.class);
        this.distView = mock(DistributionView.class);
        this.p1 = mock(Player.class);
        this.p2 = mock(Player.class);
        final Deck deck = mock(Deck.class);
        final DiscardPile pile = mock(DiscardPile.class);
        when(this.model.getPlayer1()).thenReturn(this.p1);
        when(this.model.getPlayer2()).thenReturn(this.p2);
        when(this.model.getCommonDeck()).thenReturn(deck);
        when(this.model.getDiscardPile()).thenReturn(pile);
        when(pile.getCards()).thenReturn(Collections.emptyList());
        when(this.p1.getHand()).thenReturn(Collections.emptyList());
        when(this.p2.getHand()).thenReturn(Collections.emptyList());
        this.controller = new RoundControllerImpl(
            this.tableView, 
            this.resetManager, 
            this.model, 
            this.distController, 
            this.distView
        );
    }

    @Test
    void testProcessNewRoundFullSequence() {
        this.controller.processNewRound();
        verify(this.resetManager).resetRound(eq(this.p1), eq(this.p2), any(), any());
        verify(this.model).resetForNewRound();
        verify(this.tableView).startNewRound();
        verify(this.distController).distribute(eq(this.p1), eq(this.p2), any(), any());
        verify(this.distView).refresh(any(), any());
        verify(this.tableView).repaintTable();
    }
}
