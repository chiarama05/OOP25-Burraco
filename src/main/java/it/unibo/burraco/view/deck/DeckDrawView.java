package it.unibo.burraco.view.deck;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

/**
 * View interface for deck draw interactions.
 * Implemented by UI components that need to react to draw outcomes.
 */
public interface DeckDrawView {

    /**
     * Called when a draw from the deck succeeds.
     * The implementing component should update the UI to reflect the new hand.
     *
     * @param current the player who performed the draw
     * @param hand    the updated list of cards in the player's hand
     */
    void onDrawSuccess(Player current, List<Card> hand);

    /**
     * Called when a draw attempt fails.
     * The implementing component should display the error message to the user.
     *
     * @param message a human-readable description of the error
     */
    void showDrawError(String message);
}
