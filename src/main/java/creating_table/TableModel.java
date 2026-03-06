package creating_table;

import model.player.Player;

public interface TableModel{


    Player getPlayer1();

    Player getPlayer2();

    Player getCurrentPlayer();

    Player getOpponent();

    boolean isPlayer1Turn();

    boolean isGameFinished();

    void switchTurn();

    void mustTakePot();
    
    boolean canClose();

    void confirmClosureAndWin();
}