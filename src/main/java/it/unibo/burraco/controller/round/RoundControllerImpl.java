package it.unibo.burraco.controller.round;

import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;

public final class RoundControllerImpl implements RoundController {

    private final TableView tableView;
    private final ResetManager resetManager;
    private final Player player1;
    private final Player player2;
    private final GameModel model;
    private final InitialDistributionController distributionController;
    private final InitialDistributionView distributionView;

    public RoundControllerImpl(final TableView tableView,
                                final ResetManager resetManager,
                                final Player p1,
                                final Player p2,
                                final GameModel model,
                                final InitialDistributionController distributionController,
                                final InitialDistributionView distributionView) {
        this.tableView = tableView;
        this.resetManager = resetManager;
        this.player1 = p1;
        this.player2 = p2;
        this.model = model;
        this.distributionController = distributionController;
        this.distributionView = distributionView;
    }

    @Override
    public void processNewRound() {
        this.resetManager.resetRound(
            this.player1, this.player2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());

        this.model.resetDrawnFlag();

        this.tableView.startNewRound();

        this.distributionController.distribute(
            this.player1, this.player2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());

        this.distributionView.refresh(
            this.player1.getHand(),
            this.player2.getHand(),
            this.tableView.getDiscardView(),
            this.model.getDiscardPile().getCards());

        this.tableView.refreshTurnLabel(true);
        this.tableView.refreshHandPanel(true, this.player1.getHand());
        this.tableView.getDiscardView()
            .updateDiscardPile(this.model.getDiscardPile().getCards());
        this.tableView.repaintTable();
    }
}