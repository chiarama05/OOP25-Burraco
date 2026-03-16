package core.turn;

import model.turn.Turn;
import view.table.TableView;

import core.drawcard.DrawManager;

public class TurnController {

    private final Turn turnModel;
    private final TableView view;
    private final DrawManager drawManager;

    public TurnController(Turn turnModel, TableView v, DrawManager dm) {
        this.turnModel = turnModel;
        this.view = v;
        this.drawManager = dm;
    }

    public void executeNextTurn() {
        
        turnModel.nextTurn();
        
       
        if (drawManager != null) {
            drawManager.resetTurn();
        }

       
        view.refreshTurnLabel(turnModel.isPlayer1Turn());
        view.switchHand(turnModel.isPlayer1Turn());
    }
}
