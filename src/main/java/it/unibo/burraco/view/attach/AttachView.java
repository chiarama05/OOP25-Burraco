package it.unibo.burraco.view.attach;

import it.unibo.burraco.model.player.Player;

/**
 * View interface for card attachment interactions.
 * Implemented by UI components that need to react to attach outcomes.
 */
public interface AttachView {

    /**
     * Displays an error message when an attach attempt fails.
     *
     * @param message a human-readable description of the error
     * @param title   the title of the error dialog
     */
    void showAttachError(String message, String title);

    /**
     * Refreshes the visual representation of the combination after a successful attach.
     */
    void updateCombinationVisuals();

    /**
     * Called when a card is successfully attached.
     * Updates the player's hand display.
     *
     * @param p        the player who performed the attach
     * @param isPlayer1 true if the action was performed by Player 1
     */
    void onAttachSuccess(Player p, boolean isPlayer1);

    /**
     * Called when an attach leaves the player with no cards and the pot must be taken.
     * Clears the current hand selection.
     *
     * @param p        the player who performed the attach
     * @param isPlayer1 true if the action was performed by Player 1
     */
    void onAttachTakePot(Player p, boolean isPlayer1);

    /**
     * Called when an attach triggers a game closure or a stuck state.
     * Clears the selection and refreshes the hand panel.
     *
     * @param p        the player who performed the attach
     * @param isPlayer1 true if the action was performed by Player 1
     */
    void onAttachClose(Player p, boolean isPlayer1);
}
