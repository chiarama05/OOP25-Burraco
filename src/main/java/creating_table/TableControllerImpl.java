package creating_table;

import javax.swing.SwingUtilities;

public class TableControllerImpl implements TableController {

    private final TableModelImpl model;
    private final TableViewImpl view;

    public TableControllerImpl(final TableModelImpl model, final TableViewImpl view){
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

