package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.display.GameState;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.view.table.hand.HandView;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Primary view interface — the only view interface known to the controller.
 * No Swing types, no implementation details.
 */
public interface BurracoView {
    void wakeUp(String playerName, boolean isPlayer1, List<Card> hand);
    void setMoveFuture(CompletableFuture<Move> future);
    CompletableFuture<Move> getPendingFuture();
    void refresh(GameState state);
    void showMoveError(MoveError error);
    void refreshTurnLabel(boolean isP1);
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
    void updateDiscardPile(List<Card> cards);
    void switchHand(boolean isP1);
    void startNewRound();
    void repaintTable();
    void showFinalHands(List<Card> hand1, List<Card> hand2);
    void markPotTaken(boolean isP1);
    void notifyPotTaken(String playerName, boolean isDiscard);
}