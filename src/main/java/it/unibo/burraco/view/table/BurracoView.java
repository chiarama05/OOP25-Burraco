package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.display.GameState;
import it.unibo.burraco.model.cards.Card;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Primary view interface — the only view interface known to the controller.
 * Contains no Swing types and no implementation details to preserve a clean
 * separation between the engine and the graphical layout.
 */
public interface BurracoView {

    /**
     * Initializes the view and awakes the interface for the current player.
     *
     * @param playerName the name of the player whose turn it is
     * @param isPlayer1  true if the current player is Player 1
     * @param hand       the initial list of cards in the player's hand
     */
    void wakeUp(String playerName, boolean isPlayer1, List<Card> hand);


    /**
     * Injects the future the view must complete when the player confirms an action.
     *
     * @param future the future to complete with a {@link ViewAction}
     */
    void setActionFuture(CompletableFuture<ViewAction> future);

    /**
     * Completely refreshes the table components based on the given game state snapshot.
     *
     * @param state the current immutable state of the game
     */
    void refresh(GameState state);

    /**
     * Displays a visual error message when an illegal move is attempted.
     *
     * @param error the specific move error containing the explanation message
     */
    void showMoveError(MoveError error);

    /**
     * Updates the status label indicating which player currently holds the active turn.
     *
     * @param isP1 true if it is Player 1's turn, false for Player 2
     */
    void refreshTurnLabel(boolean isP1);

    /**
     * Refreshes only the card panel representing a specific player's hand.
     *
     * @param isPlayer1 true if the hand belongs to Player 1
     * @param hand      the updated list of cards currently in that hand
     */
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);

    /**
     * Updates the graphical representation of the discard pile.
     *
     * @param cards the list of cards currently in the discard pile
     */
    void updateDiscardPile(List<Card> cards);

    /**
     * Toggles or highlights the hand view pane for the active player.
     *
     * @param isP1 true if the view should focus on Player 1
     */
    void switchHand(boolean isP1);

    /**
     * Resets the graphical table state preparing components for a new round.
     */
    void startNewRound();

    /**
     * Forces the main table frame and its sub-components to repaint themselves.
     */
    void repaintTable();

    /**
     * Reveals the final content of both players' hands at the end of a round.
     *
     * @param hand1 the remaining cards of Player 1
     * @param hand2 the remaining cards of Player 2
     */
    void showFinalHands(List<Card> hand1, List<Card> hand2);

    /**
     * Graphically marks that a player's pot has been collected.
     *
     * @param isP1 true if Player 1 took their pot, false for Player 2
     */
    void markPotTaken(boolean isP1);

    /**
     * Triggers a notification or log message informing that a player took the pot.
     *
     * @param playerName the name of the player who collected the pot
     * @param isDiscard  true if the pot was taken on a discard action, false if on the fly
     */
    void notifyPotTaken(String playerName, boolean isDiscard);
}