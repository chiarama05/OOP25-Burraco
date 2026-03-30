package it.unibo.burraco.controller.discardcard.discard;

import it.unibo.burraco.view.discardcard.discard.DiscardActionView;

/**
 * Orchestrates the interaction between the Discard View and the Discard Logic.
 * It translates user actions into requests for the controller and updates the view 
 * based on the result.
 */
public class DiscardActionController {

    private final DiscardController discardController;

    /**
     * @param discardController the logic controller that handles the discard process.
     */
    public DiscardActionController(final DiscardController discardController) {
        this.discardController = discardController;
    }

    /**
     * Handles the discard request from the view.
     * 
     * @param view the view triggering the action.
     * @param isPlayer1 true if the action was performed by player 1.
     */
    public void handle(final DiscardActionView view, final boolean isPlayer1) {
        final DiscardResult result = discardController.tryDiscard(view.getSelectedCards(isPlayer1));

        if (result.isValid()) {
            // Update the UI in case of success
            view.onDiscardSuccess(result.getCurrentPlayer(), result.getUpdatedDiscardPile(), isPlayer1);
        } else {
            // Inform the user about the error (didn't draw, multiple cards selected)
            view.onDiscardError(result.getMessage());
        }
    }
}
