package it.unibo.burraco.controller.round;

import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.BurracoView;
import it.unibo.burraco.view.table.DistributionView;

/**
 * Concrete implementation of {@link RoundController}.
 *
 * <p>Every round — including the very first — passes through
 * {@link #processNewRound()}, guaranteeing a single code path
 * from start to finish. On the first call the reset step is skipped
 * because no previous state exists to clear.</p>
 */
public final class RoundControllerImpl implements RoundController {

    private final BurracoView tableView;
    private final ResetManager resetManager;
    private final GameModel model;
    private final InitialDistributionController distributionController;
    private final DistributionView distributionView;

    private boolean isFirstRound = true;

    public RoundControllerImpl(final BurracoView tableView,
                               final ResetManager resetManager,
                               final GameModel model,
                               final InitialDistributionController distributionController,
                               final DistributionView distributionView) {
        this.tableView = tableView;
        this.resetManager = resetManager;
        this.model = model;
        this.distributionController = distributionController;
        this.distributionView = distributionView;
    }

    @Override
    public void processNewRound() {
        final Player p1 = model.getPlayer1();
        final Player p2 = model.getPlayer2();

        if (isFirstRound) {
            isFirstRound = false;
        } else {
            resetManager.resetRound(p1, p2,
                    model.getCommonDeck(),
                    model.getDiscardPile());
            model.resetForNewRound();
        }

        tableView.startNewRound();

        distributionController.distribute(p1, p2,
                model.getCommonDeck(),
                model.getDiscardPile());

        distributionView.refresh(p1.getHand(), p2.getHand());

        tableView.updateDiscardPile(model.getDiscardPile().getCards());
        tableView.refreshTurnLabel(true);
        tableView.refreshHandPanel(true, p1.getHand());
        tableView.repaintTable();
    }
}
