package it.unibo.burraco.controller.discardcard.takediscard;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

/**
 * Pure logic controller for the "Take Discard" action.
 * It acts as a bridge between the discard pile model and the drawing management logic.
 */
public class TakeDiscardController {

    private final DrawManager drawManager;
    private final Turn turnModel;
    private final DiscardPile discardPile;

    /**
     * Constructs a TakeDiscardController.
     *
     * @param drawManager  the manager that enforces draw-once-per-turn rules
     * @param turnModel    provides the current player reference
     * @param discardPile  the discard pile whose cards will be transferred to the player's hand
     */
    public TakeDiscardController(final DrawManager drawManager, final Turn turnModel, final DiscardPile discardPile) {
        this.drawManager = drawManager;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
    }

    /**
     * Attempts to transfer all cards from the discard pile to the current player's hand.
     * It relies on the DrawManager to enforce rules regarding drawing permissions.
     * 
     * @return a DrawResult indicating success or the specific reason for failure
     */
    public DrawResult tryTakeDiscard() {
        final Player current = turnModel.getCurrentPlayer();
        return drawManager.drawFromDiscard(current, discardPile.getCards());
    }
}
