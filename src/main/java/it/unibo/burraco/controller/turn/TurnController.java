package it.unibo.burraco.controller.turn;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.model.turn.Turn;

public class TurnController {

    private final Turn turnModel;
    private final DrawManager drawManager;
    private Runnable onTurnChanged;

    public TurnController(final Turn turnModel, final DrawManager dm) {
        this.turnModel = turnModel;
        this.drawManager = dm;
    }

    
    public void setOnTurnChangedListener(final Runnable listener) {
        this.onTurnChanged = listener;
    }

    public void executeNextTurn() {
       this.turnModel.nextTurn();
        
        if (this.drawManager != null) {
            this.drawManager.resetTurn();
        }

        if (this.onTurnChanged != null) {
            this.onTurnChanged.run();
        }
    }
}
