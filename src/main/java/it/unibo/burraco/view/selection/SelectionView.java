package it.unibo.burraco.view.selection;

import it.unibo.burraco.model.card.Card;
import java.util.List;

/**
 * Interface representing the view components involved in the card selection
 * and combination placement process.
 * It allows the selection manager to trigger visual updates without being
 * coupled to a specific GUI implementation.
 */
public interface SelectionView {

    /**
     * Displays an error message to the user when a selection or
     * combination move is invalid.
     *
     * @param message the descriptive error message to show.
     */
    void showSelectionError(String message);

    /**
     * Updates the player's table area by adding a new valid combination of cards.
     *
     * @param cards the list of cards forming the combination.
     * @param isP1 true if the cards belong to Player 1, false for Player 2.
     */
    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);

    /**
     * Refreshes the player's hand panel, typically called after cards
     * have been removed to form a combination or discard.
     *
     * @param isPlayer1 true to refresh Player 1's hand, false for Player 2.
     * @param hand the updated list of cards currently held by the player.
     */
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
}
