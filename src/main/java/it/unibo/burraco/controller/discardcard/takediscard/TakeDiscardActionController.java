package it.unibo.burraco.controller.discardcard.takediscard;

import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.discard.TakeDiscardActionView;
import it.unibo.burraco.controller.drawcard.DrawResult;

/**
 * Controller responsible for connecting the "Take Discard" UI events with the game logic.
 * It manages the flow of retrieving cards from the discard pile and updating the view
 * with the new player state or error messages.
 */
public class TakeDiscardActionController {

    private final TakeDiscardController takeDiscardController;
    private final Turn turnModel;
    private final DiscardPile discardPile;

    /**
     * @param takeDiscardController the underlying logic controller for taking the pile.
     * @param turnModel the model tracking the current turn state.
     * @param discardPile the model representing the pile of discarded cards.
     */
    public TakeDiscardActionController(TakeDiscardController takeDiscardController,
                                        Turn turnModel,
                                        DiscardPile discardPile) {
        this.takeDiscardController = takeDiscardController;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
    }

    /**
     * Handles the user's request to take all cards from the discard pile.
     * @param view the view component that will receive success or error notifications.
     */
    public void handle(TakeDiscardActionView view) {
        // Attempt to execute the take-discard logic
        DrawResult result = takeDiscardController.tryTakeDiscard();

        // Check if the operation was successful
        if (result.getStatus() == DrawResult.Status.SUCCESS_MULTIPLE) {
            Player current = turnModel.getCurrentPlayer();
            boolean isP1 = turnModel.isPlayer1Turn(); 

            // Update the view with the new hand of the current player
            view.onTakeDiscardSuccess(current, discardPile.getCards(), isP1);
        } else {
            // Forward the specific error status (already drawn, empty pile) to the view
            view.onTakeDiscardError(result);
        }
    }
}