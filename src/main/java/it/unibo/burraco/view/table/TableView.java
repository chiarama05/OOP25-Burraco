package it.unibo.burraco.view.table;

import javax.swing.JPanel;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.discard.DiscardViewImpl;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.hand.HandViewImpl;
import java.util.List;

public interface TableView {
    it.unibo.burraco.controller.game.GameController getGameController();

    void refreshHandPanel(Player player);
    void refreshTurnLabel(boolean isPlayer1);
    void switchHand(boolean isPlayer1Turn);
    HandViewImpl getHandViewForPlayer(Player player);
    
    
    void markPotTaken(boolean isPlayer1);
    void addCombinationToPlayerPanel(List<Card> cards, boolean player1Turn);
    void startNewRound();
    
    
    DiscardViewImpl getDiscardView();
    JPanel getDiscardPanel();
    void wireControllers(it.unibo.burraco.model.turn.Turn turnModel);
    void showScoreModal(String title, String message);
    void repaintTable();

    InitialDistributionView getInitDist();
    void showPotMessage(String playerName, boolean isDiscard);
}
