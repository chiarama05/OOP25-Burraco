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
     * @param turnModel   the model that tracks whose turn it is
     * @param dm  the draw manager whose state must be reset at the start of each turn;
     * may be {@code null} if no draw-state reset is needed
     */
    public TurnController(final Turn turnModel, final DrawManager dm) {
        this.turnModel = turnModel;
        this.drawManager = dm;
    }

    /**
     * Registers a callback that will be invoked every time the turn changes.
     *
     * @param listener the {@link Runnable} to execute after each turn transition;
     * pass {@code null} to remove the current listener
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
