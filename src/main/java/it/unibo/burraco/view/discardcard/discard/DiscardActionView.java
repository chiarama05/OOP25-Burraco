package it.unibo.burraco.view.discardcard.discard;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

/**
 * Defines the contract for the discard action UI.
 * It provides methods for retrieving user selection and handling the
 * feedback from the controller regarding the success or failure of the discard.
 */
public interface DiscardActionView {

    /**
     * Retrieves the set of cards currently selected by the player.
     *
     * @param isPlayer1 true if the current player is Player 1.
     * @return a set of selected {@link Card} objects.
     */
    List<Card> getSelectedCards(boolean isPlayer1);

    /**
     * Updates the UI state after a successful discard move.
     *
     * @param player the updated player model.
     * @param updatedPile the new state of the discard pile.
     * @param isPlayer1 true if Player 1 performed the move.
     */
    void onDiscardSuccess(Player player, List<Card> updatedPile, boolean isPlayer1);

    /**
     * Notifies the user about an error during the discard attempt.
     *
     * @param errorCode a string representing the specific rule violation.
     */
    void onDiscardError(DiscardResult.Status status);
}
