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
        ui(() -> view.refreshTurnLabel(model.isPlayer1Turn));
    }
}

