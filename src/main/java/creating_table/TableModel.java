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

    void PotFly();
    
    boolean canClose();

    void confirmClosureAndWin();
}