package it.unibo.burraco.controller.round;

import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;

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
        resetManager.resetRound(player1, player2,
            gameController.getCommonDeck(),
            gameController.getDiscardPile());

        tableView.startNewRound();

        distributionController.distribute(
            player1, player2,
            gameController.getCommonDeck(),
            gameController.getDiscardPile());

        distributionView.refresh(
            player1.getHand(),
            player2.getHand(),
            tableView.getDiscardView(),
            gameController.getDiscardPile().getCards());

        gameController.getDrawManager().resetTurn();

        tableView.refreshTurnLabel(true);
        tableView.refreshHandPanel(true, player1.getHand());
        tableView.getDiscardView()
            .updateDiscardPile(gameController.getDiscardPile().getCards());

        tableView.repaintTable();
    }
}