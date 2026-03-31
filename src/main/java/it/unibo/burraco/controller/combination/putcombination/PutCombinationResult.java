package it.unibo.burraco.controller.combination.putcombination;

import java.util.List;

import it.unibo.burraco.model.card.Card;

/**
 * Represents the outcome of an attempt to lay down a new combination.
 * This class follows a result pattern to carry both the status and the data
 * required by the UI to update the game board.
 */
public class PutCombinationResult {

    /**
     * Enumeration of all possible outcomes for a "put combination" action.
     */
    public enum Status {
        /** Player hasn't drawn a card yet. */
        NOT_DRAWN,

        /** Selection is empty. */
        NO_CARDS_SELECTED,

        /** Action leads to an illegal hand state. */
        WOULD_GET_STUCK,

        /** Rules for Set or Straight not met. */
        INVALID_COMBINATION,

        /** Action successful. */
        SUCCESS,

        /** Action successful and created a Burraco. */
        SUCCESS_BURRACO,

        /** Action led to player taking the pot. */
        SUCCESS_TAKE_POT,

        /** Action led to player closing the round. */
        SUCCESS_CLOSE,

        /** Successful move but player is now stuck. */
        SUCCESS_STUCK
    }

    private final Status status;
    private final List<Card> processedCombo;
    private final boolean isPlayer1;

    /**
     * Private constructor used by factory methods to create a result instance.
     * 
     * @param status     the outcome status
     * @param combo      the processed combination of cards, or null if failed
     * @param isPlayer1  true if the action was performed by player 1
     */
    private PutCombinationResult(final Status status, final List<Card> combo, final boolean isPlayer1) {
        this.status = status;
        this.processedCombo = combo;
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Creates an error result with the specified status.
     * 
     * @param status the reason for failure
     * @return a result instance representing a failure
     */
    public static PutCombinationResult error(final Status status) {
        return new PutCombinationResult(status, null, false);
    }

    /**
     * Creates a success result with the relevant data.
     * 
     * @param status    the success status type
     * @param combo     the validated and ordered combination
     * @param isPlayer1 true if the action was performed by player 1, false for player 2
     * @return a result instance representing a successful move
     */
    public static PutCombinationResult success(final Status status,
                                                final List<Card> combo,
                                                final boolean isPlayer1) {
        return new PutCombinationResult(status, combo, isPlayer1);
    }

    /**
     * Returns the status of this result.
     * 
     * @return the outcome status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the list of cards in the combination, or null if the action failed.
     * 
     * @return the processed combination
     */
    public List<Card> getProcessedCombo() {
        return processedCombo;
    }

    /**
     * Returns whether the action was performed by player 1.
     * 
     * @return true if the current player is player 1
     */
    public boolean isPlayer1() {
        return isPlayer1;
    }
}
