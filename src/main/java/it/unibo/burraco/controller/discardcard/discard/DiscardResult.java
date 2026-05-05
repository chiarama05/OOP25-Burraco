package it.unibo.burraco.controller.discardcard.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;

import java.util.Collections;
import java.util.List;

/**
 * A Result Object that encapsulates the outcome of a discard attempt.
 * It carries information about the validity of the move, the state of the game,
 * and data required to update the UI.
 */
public final class DiscardResult {

    private final boolean valid;
    private final boolean turnEnds;
    private final boolean gameWon;
    private final String message;
    private final List<Card> updatedDiscardPile;
    private final Player currentPlayer;

    /**
     * Constructor for a failed discard attempt.
     *
     * @param message the error message or reason for failure.
     */
    public DiscardResult(final String message) {
        this.valid = false;
        this.turnEnds = false;
        this.gameWon = false;
        this.message = message;
        this.updatedDiscardPile = null;
        this.currentPlayer = null;
    }

    /**
     * Constructor for a successful discard attempt.
     *
     * @param pile the updated state of the discard pile.
     * @param player the player who performed the action.
     * @param gameWon true if this discard triggers a round or match victory.
     */
    public DiscardResult(final List<Card> pile, final Player player, final boolean gameWon) {
        this.valid = true;
        this.turnEnds = true;
        this.gameWon = gameWon;
        this.message = null;
        this.updatedDiscardPile = pile;
        this.currentPlayer = player;
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
        if (this.updatedDiscardPile == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.updatedDiscardPile);
    }

    /**
     * @return the player object with updated state.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }
}
