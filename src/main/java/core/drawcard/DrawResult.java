package core.drawcard;
import model.card.Card;

/**
 * DrawResult represents the outcome of a draw action.
 */
public class DrawResult {

    /**
     * Enumeration representing all possible outcomes
     * of a draw operation.
     */
    public enum Status {
        SUCCESS,
        SUCCESS_MULTIPLE,
        EMPTY_DECK,
        EMPTY_DISCARD,
        ALREADY_DRAWN
    }

    /** The result status of the draw operation */
    private Status status;

    /**
     * The card drawn from the deck.
     * This is null when:
     * - The player draws multiple cards from discard
     * - The draw fails
     */
    private Card drawnCard;

    /**
     * Private constructor to enforce the use of factory methods.
     *
     * @param status The result status.
     * @param card   The drawn card (null if not applicable).
     */
    private DrawResult(Status status, Card card) {
        this.status = status;
        this.drawnCard = card;
    }

    /**
     * Creates a successful result for drawing a single card.
     *
     * @param card The drawn card.
     * @return DrawResult with SUCCESS status.
     */
    public static DrawResult success(Card card) {
        return new DrawResult(Status.SUCCESS, card);
    }

    /**
     * Creates a successful result for drawing multiple cards
     * from the discard pile.
     *
     * @return DrawResult with SUCCESS_MULTIPLE status.
     */
    public static DrawResult successMultiple() {
        return new DrawResult(Status.SUCCESS_MULTIPLE, null);
    }

    /**
     * Creates a result indicating that the deck is empty.
     *
     * @return DrawResult with EMPTY_DECK status.
     */
    public static DrawResult emptyDeck() {
        return new DrawResult(Status.EMPTY_DECK, null);
    }

    /**
     * Creates a result indicating that the discard pile is empty.
     *
     * @return DrawResult with EMPTY_DISCARD status.
     */
    public static DrawResult emptyDiscard() {
        return new DrawResult(Status.EMPTY_DISCARD, null);
    }

    /**
     * Creates a result indicating that the player
     * has already drawn during this turn.
     *
     * @return DrawResult with ALREADY_DRAWN status.
     */
    public static DrawResult alreadyDrawn() {
        return new DrawResult(Status.ALREADY_DRAWN, null);
    }

    /**
     * Returns the result status.
     *
     * @return The draw operation status.
     */
    public Status getStatus() {
        return status;
    }

     /**
     * Returns the drawn card.
     * 
     * @return The drawn card if SUCCESS,
     *         otherwise null.
     */
    public Card getDrawnCard() {
        return drawnCard;
    }
}
