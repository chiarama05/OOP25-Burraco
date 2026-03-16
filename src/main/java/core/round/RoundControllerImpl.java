package core.round;

import javax.swing.SwingUtilities;
import core.distributioncard.DistributionManagerImpl;
import model.player.PlayerImpl;
import view.controller.GameController; 
import view.distribution.InitialDistributionView;
import view.table.TableView;
import java.awt.Window;

public class RoundControllerImpl implements RoundController{

    private final TableView tableView;
    private final ResetManager resetManager;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final GameController gameController;

    public RoundControllerImpl(TableView tableView, ResetManager resetManager, 
                               PlayerImpl p1, PlayerImpl p2, GameController gameController) {
        this.tableView = tableView;
        this.resetManager = resetManager;
        this.player1 = p1;
        this.player2 = p2;
        this.gameController = gameController;
    }

    @Override
    public void processNewRound() {
        
        resetManager.resetRound(player1, player2, gameController.getCommonDeck(), gameController.getDiscardPile());

        
        tableView.startNewRound();

       
        if (tableView instanceof view.table.TableViewImpl tvImpl) {
            InitialDistributionView currentDist = tvImpl.getInitDist();

            currentDist.distribute(
                player1, player2, new DistributionManagerImpl(), 
                gameController.getCommonDeck(), tvImpl.getDiscardView(), gameController.getDiscardPile()
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
