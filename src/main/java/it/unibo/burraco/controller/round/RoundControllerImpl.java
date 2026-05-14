package it.unibo.burraco.controller.round;
 
import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.DistributionView;
import it.unibo.burraco.view.table.TableView;
 
/**
 * Orchestrates the transition to a new round.
 *
 * <p>Changes from the previous version:
 * <ul>
 *   <li>Depends on {@link DistributionView} (interface) instead of the
 *       concrete {@code TableSetUpView} class.</li>
 *   <li>The discard pile is updated <em>once</em> via
 *       {@code tableView.updateDiscardPile()} — the previous code called it
 *       twice (once inside {@code distributionView.refresh()} and once again
 *       explicitly).</li>
 *   <li>{@code distributionView.refresh()} no longer receives a
 *       {@code DiscardView} argument, eliminating View-to-View coupling.</li>
 * </ul>
 */
public final class RoundControllerImpl implements RoundController {
 
    private final TableView tableView;
    private final ResetManager resetManager;
    private final Player player1;
    private final Player player2;
    private final GameModel model;
    private final InitialDistributionController distributionController;
    private final DistributionView distributionView;   // ← interface, not concrete class
 
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
        // 1. Reset model state
        this.resetManager.resetRound(
            this.player1, this.player2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());
        this.model.resetForNewRound();
 
        // 2. Reset visual table
        this.tableView.startNewRound();
 
        // 3. Deal new cards
        this.distributionController.distribute(
            this.player1, this.player2,
            this.model.getCommonDeck(),
            this.model.getDiscardPile());
 
        // 4. Refresh both hands (no DiscardView argument — decoupled)
        this.distributionView.refresh(
            this.player1.getHand(),
            this.player2.getHand(),
            this.model.getDiscardPile().getCards());
 
        // 5. Update discard pile — single, explicit call
        this.tableView.updateDiscardPile(this.model.getDiscardPile().getCards());
 
        // 6. Refresh remaining UI elements
        this.tableView.refreshTurnLabel(true);
        this.tableView.refreshHandPanel(true, this.player1.getHand());
        this.tableView.repaintTable();
    }
}