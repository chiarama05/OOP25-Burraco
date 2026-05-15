package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.GameState;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.move.Move;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Primary view interface.
 * Depends only on:
 *  - Card (primitive domain object, unavoidable)
 *  - controller.GameState (DTO built by controller)
 *  - view.MoveError (view-owned enum)
 * No Player, no MoveResult.
 */
public interface BurracoView {

    /**
     * Called at the start of each turn.
     *
     * @param playerName display name of the current player
     * @param isPlayer1  true if it is player 1's turn
     * @param hand       the current player's hand, extracted by the controller
     */
    void wakeUp(String playerName, boolean isPlayer1, List<Card> hand);

    void setMoveFuture(CompletableFuture<Move> future);

    CompletableFuture<Move> getPendingFuture();

    void refresh(GameState state);

    void showMoveError(MoveError error);
}