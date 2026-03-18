package view.table;

import javax.swing.JPanel;
import model.player.Player;
import model.card.Card;
import view.discard.DiscardViewImpl;
import view.hand.handImpl;
import java.util.List;

public interface TableView {
    core.controller.GameController getGameController();

    void refreshHandPanel(Player player);
    void refreshTurnLabel(boolean isPlayer1);
    void switchHand(boolean isPlayer1Turn);
    handImpl getHandViewForPlayer(Player player);
    
    
    void markPotTaken(boolean isPlayer1);
    void addCombinationToPlayerPanel(List<Card> cards, boolean player1Turn);
    void startNewRound();
    
    
    DiscardViewImpl getDiscardView();
    JPanel getDiscardPanel();
    void wireControllers(model.turn.Turn turnModel);
}
