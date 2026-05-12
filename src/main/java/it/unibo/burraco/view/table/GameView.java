package it.unibo.burraco.view.table;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.hand.HandView;
import it.unibo.burraco.view.pot.PotView;
import java.util.List;

public interface GameView extends PotView {

    void refreshTurnLabel(boolean isP1);
    void switchHand(boolean isP1);
    void startNewRound();
    void repaintTable();
    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);
    void updateDiscardPile(List<Card> cards);
    HandView getPlayer1HandView();
    HandView getPlayer2HandView();
    HandView getHandViewForCurrentPlayer(boolean isPlayer1);
}