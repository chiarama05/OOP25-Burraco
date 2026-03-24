package it.unibo.burraco.controller.turn;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.model.turn.Turn;

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