package model.turn;

import model.player.Player;

public interface TurnManager {

    void nextTurn();

    public void resetForNewRound();

    void tryClosure();

    Player getCurrentPlayer();
}
