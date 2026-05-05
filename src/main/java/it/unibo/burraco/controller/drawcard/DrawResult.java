package it.unibo.burraco.controller.drawcard;

import it.unibo.burraco.model.card.Card;

/**
 * Immutable object representing the outcome of a draw operation.
 * It provides status information and the drawn card data to the caller.
 */
public final class DrawResult {

    /**
     * Enumerates all possible results of a draw attempt.
     */
    public enum Status {
        /** Single card drawn from deck. */
        SUCCESS,
        /** Multiple cards taken from discard pile. */
        SUCCESS_MULTIPLE,
        /** Failed: Deck has no cards. */
        EMPTY_DECK,
        /** Failed: Discard pile is empty. */
        EMPTY_DISCARD,
        /** Failed: Player already drew this turn. */
        ALREADY_DRAWN
    }

    private final Status status;
    private final Card drawnCard;

    /**
     * Constructor for a successful single card draw (usually from the deck).
     *
     * @param card the card that was drawn.
     */
    public DrawResult(final Card card) {
        this.status = Status.SUCCESS;
        this.drawnCard = card;
    }

    /**
     * Constructor for status-only results (errors or multiple card draws).
     *
     * @param status the specific status of the draw operation.
     */
    public DrawResult(final Status status) {
        this.status = status;
        this.drawnCard = null;
    }

    /**
     * @return the status of the operation
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the card drawn if status is SUCCESS; null otherwise.
     */
    public Card getDrawnCard() {
        return drawnCard;
    }
}
