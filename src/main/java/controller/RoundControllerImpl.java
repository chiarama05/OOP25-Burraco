package controller;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import core.distributioncard.DistributionManagerImpl;
import core.resetround.ResetManager;
import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.player.PlayerImpl;
import view.distribution.InitialDistributionView;
import view.table.TableView;
import java.awt.Window;

public class RoundControllerImpl implements RoundController{

    private final TableView tableView;
    private final ResetManager resetManager;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final DeckImpl commonDeck;
    private final DiscardPile discardPile;

    public RoundControllerImpl(TableView tableView, ResetManager resetManager, 
                               PlayerImpl p1, PlayerImpl p2, 
                               DeckImpl deck, DiscardPile discardPile) {
        this.tableView = tableView;
        this.resetManager = resetManager;
        this.player1 = p1;
        this.player2 = p2;
        this.commonDeck = deck;
        this.discardPile = discardPile;
    }

    @Override
    public void processNewRound() {
        
        resetManager.resetRound(player1, player2, commonDeck, discardPile);

        tableView.startNewRound();

        InitialDistributionView currentDist = ((view.table.TableViewImpl)tableView).getInitDist();

        currentDist.distribute(
        player1, player2, new DistributionManagerImpl(), 
        commonDeck, tableView.getDiscardView(), discardPile
    );
        
        tableView.getDrawManager().resetTurn();
        
        tableView.refreshTurnLabel(true);
        tableView.refreshHandPanel(player1);

        tableView.getDiscardView().updateDiscardPile(discardPile.getCards());

        if (tableView instanceof view.table.TableViewImpl tvImpl) {
        tvImpl.getDiscardPanel().revalidate();
        tvImpl.getDiscardPanel().repaint();
        // Recupera il frame principale e forza un refresh totale
        Window w = SwingUtilities.getWindowAncestor(tvImpl.getDiscardPanel());
        if (w != null) {
            w.revalidate();
            w.repaint();
        }
    }
    }
}
