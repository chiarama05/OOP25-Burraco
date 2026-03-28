package it.unibo.burraco.controller.round;

import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;

/**
 * Concrete implementation of the RoundController.
 * It acts as an orchestrator that coordinates the reset logic, the distribution logic,
 * and the synchronization of multiple view components.
 */
public class RoundControllerImpl implements RoundController {

    private final TableView tableView;
    private final ResetManager resetManager;
    private final Player player1;
    private final Player player2;
    private final GameController gameController;
    private final InitialDistributionController distributionController;
    private final InitialDistributionView distributionView;

    public RoundControllerImpl(TableView tableView, ResetManager resetManager,
                               Player p1, Player p2, GameController gameController,
                               InitialDistributionController distributionController,
                               InitialDistributionView distributionView) { 
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
        resetManager.resetRound(player1, player2,
            gameController.getCommonDeck(),
            gameController.getDiscardPile());

        // Prepare the UI for the fresh start
        tableView.startNewRound();

        // Execute the card distribution logic (Hands, Pots, and first Discard)
        distributionController.distribute(
            player1, player2,
            gameController.getCommonDeck(),
            gameController.getDiscardPile());

        // Update the high-level distribution view
        distributionView.refresh(
            player1.getHand(),
            player2.getHand(),
            tableView.getDiscardView(),
            gameController.getDiscardPile().getCards());

        // Reset turn-specific constraints (drawing flags)
        gameController.getDrawManager().resetTurn();

        // Final UI synchronization for the starting player
        tableView.refreshTurnLabel(true); // Player 1 starts by default
        tableView.refreshHandPanel(true, player1.getHand());
        tableView.getDiscardView()
            .updateDiscardPile(gameController.getDiscardPile().getCards());

        // Force a UI refresh to ensure all components are rendered correctly
        tableView.repaintTable();
    }
}