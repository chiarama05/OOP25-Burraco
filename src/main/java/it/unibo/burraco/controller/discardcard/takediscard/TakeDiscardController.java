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

    public TakeDiscardController(DrawManager drawManager, Turn turnModel, DiscardPile discardPile) {
        this.drawManager = drawManager;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
    }

    /**
     * Attempts to transfer all cards from the discard pile to the current player's hand.
     * It relies on the DrawManager to enforce rules regarding drawing permissions.
     * * @return a {@link DrawResult} indicating success or the specific reason for failure.
     */
    public DrawResult tryTakeDiscard() {
        Player current = turnModel.getCurrentPlayer();
        // Delegates the card transfer logic to the central DrawManager
        return drawManager.drawFromDiscard(current, discardPile.getCards());
    }
}
