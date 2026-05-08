package it.unibo.burraco.view.table;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.hand.HandView;
import java.util.List;

/**
 * View interface used by controllers during the game.
 * Contains only visual update operations — no Swing widget accessors.
 */
public interface GameView {

    void refreshTurnLabel(boolean isP1);

    void switchHand(boolean isP1);

    void startNewRound();

    void showScoreModal(String title, String message);

    void repaintTable();

    void refreshHandPanel(boolean isPlayer1, List<Card> hand);

    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);

    HandView getPlayer1HandView();

    HandView getPlayer2HandView();

    HandView getHandViewForCurrentPlayer(boolean isPlayer1);
}
