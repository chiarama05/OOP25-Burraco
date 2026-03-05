package model.turn;

import model.player.Player;

public interface TurnManager {

    void nextTurn();

    void tryClosure();

    Player getCurrentPlayer();
}
