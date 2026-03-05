package model.turn;


import model.player.Player;
import creating_table.TableModelImpl;
import view.table.TableViewImpl;

public class TurnManagerImpl implements TurnManager{

    private final TableModelImpl model;
    private final TableViewImpl view;

    public TurnManagerImpl(final TableModelImpl model, final TableViewImpl view){

        this.model=model;
        this.view=view;

        view.refreshTurnLabel(model.isPlayer1Turn());
        view.refreshHandPanel();
    }

    public Player getCurrentPlayer(){
        return model.getCurrentPlayer();
    }

    @Override
    public void nextTurn(){
        model.switchTurn();
        view.refreshTurnLabel(model.isPlayer1Turn());
        view.switchHand(model.isPlayer1Turn());

        model.mustTakePot();
    }

    @Override
    public void tryClosure(){
        if(!model.canClose()){
            view.showNotValideClosure();
            return;
        }
        model.confirmClosureAndWin();
        view.showWinExit(model.isPlayer1Turn());
    }
}
