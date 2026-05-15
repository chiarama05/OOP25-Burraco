package it.unibo.burraco.controller.round;

import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.BurracoView;
import it.unibo.burraco.view.table.DistributionView;

/**
 * Concrete implementation of {@link RoundController}.
 */
public final class RoundControllerImpl implements RoundController {

    private final BurracoView tableView;
    private final ResetManager resetManager;
    private final GameModel model;
    private final InitialDistributionController distributionController;
    private final DistributionView distributionView;

    public RoundControllerImpl(final BurracoView tableView,
                                final ResetManager resetManager,
                                final GameModel model,
                                final InitialDistributionController distributionController,
                                final DistributionView distributionView) {
        this.tableView              = tableView;
        this.resetManager           = resetManager;
        this.model                  = model;
        this.distributionController = distributionController;
        this.distributionView       = distributionView;
    }

    @Override
    public void processNewRound() {
        final Player p1 = model.getPlayer1();
        final Player p2 = model.getPlayer2();

        this.resetManager.resetRound(p1, p2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());
        this.model.resetForNewRound();

        this.tableView.startNewRound();

        this.distributionController.distribute(p1, p2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());

        this.distributionView.refresh(p1.getHand(), p2.getHand());

        this.tableView.updateDiscardPile(this.model.getDiscardPile().getCards());
        this.tableView.refreshTurnLabel(true);
        this.tableView.refreshHandPanel(true, p1.getHand());
        this.tableView.repaintTable();
    }
}