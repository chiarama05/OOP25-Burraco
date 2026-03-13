package view.table;

import javax.swing.JPanel;

import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;

public interface TableView {

    void startNewRound();

    void refreshTurnLabel(boolean turnoGiocatore1);

    void refreshHandPanel(model.player.Player player);

    void showPotFly();

    void showPotnextTurn();

    void showNotValideClosure();

    void showWinExit(boolean player1Won);
    
    DrawManager getDrawManager();

    SelectionCardManager getSelectionManager();

    JPanel getDiscardPanel();

    public view.discard.DiscardViewImpl getDiscardView();
}
