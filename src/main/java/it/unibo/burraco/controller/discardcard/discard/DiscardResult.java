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
    public enum Status {
        NOT_DRAWN,
        SELECT_ONE,
        NOT_SELECTED,
        NOT_IN_HAND,
        NO_BURRACO_ERROR,
        SUCCESS
    }

    private final boolean valid;
    private final boolean gameWon;
    private final Status status;
    private final List<Card> updatedDiscardPile;
    private final Player currentPlayer;

    public DiscardResult(final Status status) {
        this.valid = false;
        this.gameWon = false;
        this.status = status;
        this.updatedDiscardPile = null;
        this.currentPlayer = null;
    }

    public DiscardResult(final List<Card> pile, final Player player, final boolean gameWon) {
        this.valid = true;
        this.gameWon = gameWon;
        this.status = Status.SUCCESS;
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
     * @return true if the move resulted in winning the round or game.
     */
    public boolean isGameWon() {
        return this.gameWon;
    }

    public Status  getStatus() { 
        return this.status; 
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
