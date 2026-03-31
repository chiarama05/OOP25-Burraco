package it.unibo.burraco.controller.round;

import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;

/**
 * Concrete implementation of the {@link RoundController}.
 * It acts as an orchestrator that coordinates the reset logic, the distribution logic,
 * and the synchronization of multiple view components.
 */
public final class RoundControllerImpl implements RoundController {

    private final TableView tableView;
    private final ResetManager resetManager;
    private final Player player1;
    private final Player player2;
    private final GameController gameController;
    private final InitialDistributionController distributionController;
    private final InitialDistributionView distributionView;

    /**
     * @param tableView            the main table view
     * @param resetManager         the manager for resetting round state
     * @param p1                   the first player
     * @param p2                   the second player
     * @param gameController       the main game controller
     * @param distributionController the controller for card distribution
     * @param distributionView     the view for the distribution phase
     */
    public RoundControllerImpl(final TableView tableView,
                               final ResetManager resetManager,
                               final Player p1,
                               final Player p2,
                               final GameController gameController,
                               final InitialDistributionController distributionController,
                               final InitialDistributionView distributionView) {
        this.tableView = tableView;
        this.resetManager = resetManager;
        this.player1 = p1;
        this.player2 = p2;
        this.gameController = gameController;
        this.distributionController = distributionController;
        this.distributionView = distributionView;
    }

    @Override
    public void processNewRound() {
        // Clean the state of all domain entities
        this.resetManager.resetRound(this.player1, this.player2,
            this.gameController.getCommonDeck(),
            this.gameController.getDiscardPile());

        // Prepare the UI for the fresh start
        this.tableView.startNewRound();

        // Execute the card distribution logic (Hands, Pots, and first Discard)
        this.distributionController.distribute(
            this.player1, this.player2,
            this.gameController.getCommonDeck(),
            this.gameController.getDiscardPile());

        // Update the high-level distribution view
        this.distributionView.refresh(
            this.player1.getHand(),
            this.player2.getHand(),
            this.tableView.getDiscardView(),
            this.gameController.getDiscardPile().getCards());

        // Reset turn-specific constraints (drawing flags)
        this.gameController.getDrawManager().resetTurn();

        // Final UI synchronization for the starting player
        this.tableView.refreshTurnLabel(true); // Player 1 starts by default
        this.tableView.refreshHandPanel(true, this.player1.getHand());
        this.tableView.getDiscardView()
            .updateDiscardPile(this.gameController.getDiscardPile().getCards());

        // Force a UI refresh to ensure all components are rendered correctly
        this.tableView.repaintTable();
    }
}
