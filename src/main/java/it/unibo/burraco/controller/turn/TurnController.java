package it.unibo.burraco.controller.turn;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.model.turn.Turn;

/**
 * Controller responsible for advancing the game to the next player's turn.
 */
public final class TurnController {

    private final Turn turnModel;
    private final DrawManager drawManager;
    private Runnable onTurnChanged;

    /**
     * Constructs a TurnController.
     *
     * @param turnModel the turn model to manage
     * @param dm the draw manager to reset each turn
     */
    public TurnController(final Turn turnModel, final DrawManager dm) {
        this.turnModel = turnModel;
        this.drawManager = dm;
    }

    /**
     * Registers a callback for turn changes.
     *
     * @param listener the runnable to execute after turn transition
     */
    public void setOnTurnChangedListener(final Runnable listener) {
        this.onTurnChanged = listener;
    }

    /**
     * Advances the game to the next player's turn.
     */
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
