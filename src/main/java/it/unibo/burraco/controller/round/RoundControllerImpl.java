package it.unibo.burraco.controller.round;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;
import java.awt.Window;


public class RoundControllerImpl implements RoundController{

    private final TableView tableView;
    private final ResetManager resetManager;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final GameController gameController;
    private final InitialDistributionController distributionController;


    public RoundControllerImpl(TableView tableView, ResetManager resetManager, 
                               PlayerImpl p1, PlayerImpl p2, GameController gameController) {
        this.tableView = tableView;
        this.resetManager = resetManager;
        this.player1 = p1;
        this.player2 = p2;
        this.gameController = gameController;
        this.distributionController = new InitialDistributionController(new DistributionManagerImpl());
    }

    @Override
    public void processNewRound() {
        
        resetManager.resetRound(player1, player2, gameController.getCommonDeck(), gameController.getDiscardPile());

        
        tableView.startNewRound();

       
        if (tableView instanceof it.unibo.burraco.view.table.TableViewImpl tvImpl) {
            InitialDistributionView currentDist = tvImpl.getInitDist();

            distributionController.distribute(
    player1, player2,
    gameController.getCommonDeck(),
    gameController.getDiscardPile()
);

// 2. View si aggiorna
currentDist.refresh(
    player1, player2,
    tvImpl.getDiscardView(),
    gameController.getDiscardPile()
);
            
           
            gameController.getDrawManager().resetTurn();
            
            
            tableView.refreshTurnLabel(true);
            tableView.refreshHandPanel(player1);
            tvImpl.getDiscardView().updateDiscardPile(gameController.getDiscardPile().getCards());

            tvImpl.getDiscardPanel().revalidate();
            tvImpl.getDiscardPanel().repaint();
            
            Window w = SwingUtilities.getWindowAncestor(tvImpl.getDiscardPanel());
            if (w != null) {
                w.revalidate();
                w.repaint();
            }
        }
    }
}
