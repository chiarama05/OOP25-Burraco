package it.unibo.burraco.controller.round;
 
import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.DistributionView;
import it.unibo.burraco.view.table.TableView;
 
/**
 * Concrete implementation of {@link RoundController}.
 * This class orchestrates the complete transition between rounds by coordinating 
 * the reset of the model, the redistribution of cards, and the synchronization 
 * of the graphical interfaces.
 */
public final class RoundControllerImpl implements RoundController {
 
    private final TableView tableView;
    private final ResetManager resetManager;
    private final Player player1;
    private final Player player2;
    private final GameModel model;
    private final InitialDistributionController distributionController;
    private final DistributionView distributionView;   // ← interface, not concrete class
 
    /**
     * Constructs a RoundControllerImpl.
     * 
     * @param tableView             the main table view to be updated
     * @param resetManager          the component responsible for clearing model data
     * @param p1                    the first player
     * @param p2                    the second player
     * @param model                 the game model providing access to deck and discard pile
     * @param distributionController the controller that handles the physical dealing of cards
     * @param distributionView      the interface for refreshing card visuals after distribution
     */
    public RoundControllerImpl(final TableView tableView,
                                final ResetManager resetManager,
                                final Player p1,
                                final Player p2,
                                final GameModel model,
                                final InitialDistributionController distributionController,
                                final DistributionView distributionView) {
        this.tableView             = tableView;
        this.resetManager          = resetManager;
        this.player1               = p1;
        this.player2               = p2;
        this.model                 = model;
        this.distributionController = distributionController;
        this.distributionView      = distributionView;
    }

    @Override
    public void processNewRound() {
        this.resetManager.resetRound(
            this.player1, this.player2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());
        this.model.resetForNewRound();
 
        this.tableView.startNewRound();
 
        this.distributionController.distribute(
            this.player1, this.player2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());

        this.distributionView.refresh(
            this.player1.getHand(),
            this.player2.getHand(),
            this.model.getDiscardPile().getCards());
 
        this.tableView.updateDiscardPile(this.model.getDiscardPile().getCards());

        this.tableView.refreshTurnLabel(true);
        this.tableView.refreshHandPanel(true, this.player1.getHand());
        this.tableView.repaintTable();
    }
}
