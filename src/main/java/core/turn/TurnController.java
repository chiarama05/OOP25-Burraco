package core.turn;

import model.turn.Turn;
import core.drawcard.DrawManager;

public class TurnController {

    private final Turn turnModel;
    private final DrawManager drawManager;
    private Runnable onTurnChanged;

    public TurnController(Turn turnModel, DrawManager dm) {
        this.turnModel = turnModel;
        this.drawManager = dm;
    }

    
    public void setOnTurnChangedListener(Runnable listener) {
        this.onTurnChanged = listener;
    }

    public void executeNextTurn() {
       
        turnModel.nextTurn();
        
        if (drawManager != null) {
            drawManager.resetTurn();
        }

        if (onTurnChanged != null) {
            onTurnChanged.run();
        }
    }
}