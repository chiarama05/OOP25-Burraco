package it.unibo.burraco.controller.turn;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.model.turn.Turn;


/**
 * Controller responsible for advancing the game to the next player's turn.
 * <p>
 * It delegates the actual turn switch to the {@link Turn} model, resets the draw state
 * via {@link DrawManager}, and fires an optional listener so that registered UI components
 * can react to the change (e.g. switching the displayed hand).
 * </p>
 */
public class TurnController {

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
     * <p>
     * Typically used to trigger a UI switch (e.g. showing the new player's hand).
     * Replaces any previously registered listener.
     * </p>
     *
     * @param listener the {@link Runnable} to execute after each turn transition;
     * pass {@code null} to remove the current listener
     */
    public void setOnTurnChangedListener(final Runnable listener) {
        this.onTurnChanged = listener;
    }


    /**
     * Advances the game to the next player's turn.
     * <p>
     * Sequence of operations:
     * <ol>
     *   <li>Calls {@link Turn#nextTurn()} on the model.</li>
     *   <li>Resets the draw state via {@link DrawManager#resetTurn()} (if not {@code null}).</li>
     *   <li>Fires the registered turn-changed listener (if not {@code null}).</li>
     * </ol>
     * </p>
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
