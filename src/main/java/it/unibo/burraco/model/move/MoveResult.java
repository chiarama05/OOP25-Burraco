package it.unibo.burraco.model.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.burraco.model.cards.Card;

/**
 * Represents the outcome of a {@link Move} execution.
 * It encapsulates the success or failure status, the cards affected by the action, 
 * and identifying information about the player who performed the move.
 */
public final class MoveResult {

    /**
     * Enumeration of all possible outcomes for a move, including error states
     * and specific success triggers (like taking a pot or winning).
     */
    public enum Status {
        NOT_DRAWN,
        ALREADY_DRAWN,
        WRONG_PLAYER,
        NO_CARDS_SELECTED,
        INVALID_COMBINATION,
        WOULD_GET_STUCK,
        INVALID_MOVE,
        SUCCESS,
        SUCCESS_BURRACO,
        SUCCESS_TAKE_POT,
        SUCCESS_CLOSE,
        SUCCESS_STUCK,
        ROUND_WON
    }

    private final Status status;
    private final List<Card> processedCards;
    private final boolean player1;

    
    private MoveResult(final Status status, final List<Card> cards, final boolean player1) {
        this.status = status;
        this.processedCards = new ArrayList<>(cards);
        this.player1 = player1;
    }

    /**
     * Creates a generic success result.
     * @return a SUCCESS MoveResult
     */
    public static MoveResult ok() {
        return new MoveResult(Status.SUCCESS, Collections.emptyList(), false);
    }

    /**
     * Creates an error result with a specific status.
     * @param s the error status
     * @return an error MoveResult
     */
    public static MoveResult error(final Status s) {
        return new MoveResult(s, Collections.emptyList(), false);
    }

    /**
     * Creates a detailed success result.
     * @param s     the specific success status
     * @param cards the cards involved in the operation
     * @param p1    true if the actor was player 1, false otherwise
     * @return a detailed MoveResult
     */
    public static MoveResult success(final Status s, final List<Card> cards, final boolean p1) {
        return new MoveResult(s, cards, p1);
    }

    /**
     * Checks if the status represents a successful operation.
     * @return true if the move was applied, false if it was rejected
     */
    public boolean isValid() {
        return status == Status.SUCCESS
            || status == Status.SUCCESS_BURRACO
            || status == Status.SUCCESS_TAKE_POT
            || status == Status.SUCCESS_CLOSE
            || status == Status.SUCCESS_STUCK
            || status == Status.ROUND_WON;
    }

    /**
     * @return the specific status of the result
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return an unmodifiable list of cards processed during the move
     */
    public List<Card> getProcessedCards() {
        return Collections.unmodifiableList(processedCards);
    }

    /** 
     * @return true if player 1 performed the move, false if player 2
     */
    public boolean isPlayer1() { 
        return player1; 
    }
}
