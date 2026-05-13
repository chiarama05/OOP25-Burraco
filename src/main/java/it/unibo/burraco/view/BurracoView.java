package it.unibo.burraco.view;

import it.unibo.burraco.model.GameState;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
import java.util.concurrent.CompletableFuture;

public interface BurracoView {

    void wakeUp(Player player, boolean isPlayer1);

    void setMoveFuture(CompletableFuture<Move> future);

    CompletableFuture<Move> getPendingFuture();

    void refresh(GameState state);

    void showMoveError(MoveResult error);

    void showWinner(Player winner);
}