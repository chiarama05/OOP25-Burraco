package core.resetround;

import javax.swing.JPanel;

import core.distributioncard.DistributionManagerImpl;
import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.player.PlayerImpl;
import view.distribution.InitialDistributionView;
import view.table.TableView;

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

        // 1. Reset logico
        resetManager.resetRound(player1, player2, commonDeck, discardPile);

        // 2. Pulizia grafica
        tableView.startNewRound();

        // 3. Ridistribuzione
        // Usiamo il selection manager che ora è visibile tramite l'interfaccia TableView
        InitialDistributionView initDist = new InitialDistributionView(
        tableView.getDiscardPanel(), 
        tableView.getSelectionManager()
    );
        initDist.distribute(player1, player2, new DistributionManagerImpl(), 
                           commonDeck, null, discardPile);
        
        // 4. Reset stato partita tramite DrawManager
        tableView.getDrawManager().resetTurn();
        
        // 5. Refresh finale UI
        tableView.refreshTurnLabel(true);
        tableView.refreshHandPanel(player1);
    }
}
