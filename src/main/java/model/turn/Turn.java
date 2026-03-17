package model.turn;

import model.player.Player;

public interface Turn {

    void nextTurn();

    Player getCurrentPlayer();

    boolean isPlayer1Turn();

    boolean canClose();

    boolean isGameFinished();

    void setGameFinished(boolean finished);

    void resetForNewRound(); 

    Player getPlayer1();
    
    Player getPlayer2();
}
