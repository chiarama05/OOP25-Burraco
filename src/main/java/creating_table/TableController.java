package creating_table;

public interface TableController {

    void start();

    /* Change the turn, it delegates to TableModelImpl */
    void endTurn();

    void onPotFly();

    /* When the Pot is going to be taken in the next turn */
    void onPotNextTurn();

    /* When the player tries to close the game */
    void attemptClosure();

    /* Syncronize with Model */
    void refreshTurnLabel();
}
