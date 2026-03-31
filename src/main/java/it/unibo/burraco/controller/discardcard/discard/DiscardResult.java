package it.unibo.burraco.controller.discardcard.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

/**
 * A Result Object that encapsulates the outcome of a discard attempt.
 * It carries information about the validity of the move, the state of the game,
 * and data required to update the UI.
 */
public class DiscardResult {

    private final boolean valid;
    private final boolean turnEnds;
    private final boolean gameWon;
    private final String message;
    private final List<Card> updatedDiscardPile;
    private final Player currentPlayer;

    /**
     * Private constructor to enforce the use of static factory methods.
     *
     * @param valid              if the move is valid
     * @param turnEnds           if the turn is finished
     * @param gameWon            if the game is won
     * @param message            error message
     * @param updatedDiscardPile updated pile
     * @param currentPlayer      updated player
     */
    private DiscardResult(final boolean valid, final boolean turnEnds, final boolean gameWon,
                          final String message, final List<Card> updatedDiscardPile,
                          final Player currentPlayer) {
        this.valid = valid;
        this.turnEnds = turnEnds;
        this.gameWon = gameWon;
        this.message = message;
        this.updatedDiscardPile = updatedDiscardPile;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Creates a failed discard result with an error message.
     *
     * @param message the error key or description.
     * @return a failed DiscardResult.
     */
    public static DiscardResult error(final String message) {
        return new DiscardResult(false, false, false, message, null, null);
    }

    /**
     * Creates a successful discard result.
     *
     * @param pile the updated state of the discard pile.
     * @param player the player who performed the action.
     * @param gameWon true if this discard triggers a round/match victory.
     * @return a successful DiscardResult.
     */
    public static DiscardResult success(final List<Card> pile, final Player player,
                                        final boolean gameWon) {
        return new DiscardResult(true, true, gameWon, null, pile, player);
    }

    /**
     * @return true if the discard move was legal.
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * @return true if the current player's turn should conclude.
     */
    public boolean isTurnEnds() {
        return this.turnEnds;
    }

    /**
     * @return true if the move resulted in winning the round or game.
     */
    public boolean isGameWon() {
        return this.gameWon;
    }

    /**
     * @return the error message if the move was invalid.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return the state of the discard pile after the move.
     */
    public List<Card> getUpdatedDiscardPile() {
        return this.updatedDiscardPile;
    }

    /**
     * @return the player object with updated state.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }
}
