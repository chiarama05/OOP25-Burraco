package creating_table;

import javax.swing.SwingUtilities;

public class TableControllerImpl implements TableController {

    private final TableModel model;
    private final TableView view;

    public TableControllerImpl(final TableModel model, final TableView view){
        this.model=model;
        this.view=view;
    }

    @Override
    public void start(){
        refreshTurnLabel();
    }

    @Override
    public void endTurn(){
        if(model.isGameFinished()){
            return;
        }
        model.switchTurn();
        refreshTurnLabel();
    }

    @Override
    public void refreshTurnLabel(){
        ui(() -> view.refreshTurnLabel(model.isPlayer1Turn()));
    }

    @Override
    public void onPotFly(){
        if(model.isGameFinished()){
            return;
        }
        model.PotFly();
        ui(view::showPotFly);
    }

    @Override
    public void onPotNextTurn(){
        if(model.isGameFinished()){
            return;
        }
        model.mustTakePot();
        ui(view::showPotnextTurn);
    }

    @Override
    public void attemptClosure(){
        if(model.isGameFinished()){
            return;
        }
        if(!model.canClose()){
            ui(view::showNotValideClosure);
            return;
        }
        final boolean player1Wins=model.isPlayer1Turn();
        model.confirmClosureAndWin();
        if(model.isGameFinished()){
            ui(() -> view.showWinExit(player1Wins));
        }
    }

    private void ui(final Runnable r){
        if(SwingUtilities.isEventDispatchThread()){
            r.run();
        }
        else{
            SwingUtilities.invokeLater(r);
        }
    }

}

