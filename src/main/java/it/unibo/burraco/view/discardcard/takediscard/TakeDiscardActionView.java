package it.unibo.burraco.view.discardcard.takediscard;

import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

/**
 * Interface that defines the necessary methods for the controller to interact
 * with the "Take Discard" UI logic.
 */
public interface TakeDiscardActionView {

    /**
     * Updates the UI when the player successfully picks up the discard pile.
     *
     * @param current the player who took the cards.
     * @param updatedPile the new state of the discard pile.
     * @param isPlayer1 true if the current player is Player 1.
     */
    void onTakeDiscardSuccess(Player current, List<Card> updatedPile, boolean isPlayer1);

    /**
     * Handles the display of error messages when the action is not allowed.
     *
     * @param result the result object containing the error status.
     */
    void onTakeDiscardError(DrawResult result);
}
