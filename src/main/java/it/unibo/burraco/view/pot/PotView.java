package it.unibo.burraco.view.pot;

import it.unibo.burraco.model.card.Card;
import java.util.List;


/**
 * View contract for pot-related UI updates.
 * <p>
 * Implementations are responsible for displaying feedback when a player draws their pot
 * and for reflecting the change in the table layout.
 * </p>
 */
public interface PotView {

        /**
     * Displays a message informing the player that they have drawn their pot.
     *
     * @param playerName the name of the player who took the pot
     * @param isDiscard  {@code true} if the pot was triggered by a discard action
     *                   (the new cards will be visible next turn);
     *                   {@code false} if the pot was taken on-the-fly (cards visible immediately)
     */
    void showPotMessage(String playerName, boolean isDiscard);

    /**
     * Updates the visual indicator on the player's combination panel to show
     * that the pot has been collected.
     *
     * @param isP1 {@code true} to update Player 1's panel, {@code false} for Player 2's
     */
    void markPotTaken(boolean isP1);

    /**
     * Refreshes the hand panel to display the updated list of cards after the pot draw.
     * This method is only called when the pot draw is triggered outside of a discard action.
     *
     * @param isPlayer1 {@code true} to refresh Player 1's hand panel, {@code false} for Player 2's
     * @param hand      the updated list of cards in the player's hand
     */
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
}
