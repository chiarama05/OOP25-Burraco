package creating_table;

public interface TableView {

    void refreshTurnLabel(boolean turnoGiocatore1);

    void showPotFly();

    void showPotnextTurn();

    void showNotValideClosure();

    void showWinExit(boolean player1Won);
    
}
