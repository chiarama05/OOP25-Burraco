package it.unibo.burraco.controller.combination.putcombination;

import it.unibo.burraco.model.card.Card;
import java.util.List;

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
        NOT_DRAWN,              // Player hasn't drawn a card yet
        NO_CARDS_SELECTED,      // Selection is empty
        WOULD_GET_STUCK,        // Action leads to an illegal hand state
        INVALID_COMBINATION,    // Rules for Set or Straight not met
        SUCCESS,                // Action successful
        SUCCESS_BURRACO,        // Action successful and created a Burraco
        SUCCESS_TAKE_POT,       // Action led to player taking the pot
        SUCCESS_CLOSE,          // Action led to player closing the round
        SUCCESS_STUCK           // Successful move but player is now stuck
    }

    private final Status status;
    private final List<Card> processedCombo; 
    private final boolean isPlayer1;

    /**
     * Private constructor used by factory methods to create a result instance.
     */
    private PutCombinationResult(Status status, List<Card> combo, boolean isPlayer1) {
        this.status = status;
        this.processedCombo = combo;
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Creates an error result with the specified status.
     * @param status the reason for failure
     * @return a result instance representing a failure
     */
    public static PutCombinationResult error(Status status) {
        return new PutCombinationResult(status, null, false);
    }

    /**
     * Creates a success result with the relevant data.
     * @param status the success status type
     * @param combo the validated and ordered combination
     * @param isPlayer1 true if the action was performed by player 1, false for player 2
     * @return a result instance representing a successful move
     */
    public static PutCombinationResult success(Status status, List<Card> combo, boolean isPlayer1) {
        return new PutCombinationResult(status, combo, isPlayer1);
    }

    public Status getStatus() { 
        return status; 
    }

    /** @return the list of cards in the combination, or null if failed */
    public List<Card> getProcessedCombo() { 
        return processedCombo; 
    }

    /** @return true if the current player is player 1 */
    public boolean isPlayer1() { 
        return isPlayer1; 
    }
}
