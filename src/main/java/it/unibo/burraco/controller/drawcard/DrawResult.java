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

    final private Status status;
    final private Card drawnCard;

    /**
     * Private constructor to maintain control over instance creation via factory methods.
     * 
     * @param status the status of the draw operation
     * @param card   the card drawn, if applicable
     */
    private DrawResult(final Status status, final Card card) {
        this.status = status;
        this.drawnCard = card;
    }

    /**
     * @param card the drawn card
     * @return a success result for a single card draw
     */
    public static DrawResult success(Card card) {
        return new DrawResult(Status.SUCCESS, card);
    }

    /**
     * @return a success result for multiple cards from discard 
    */
    public static DrawResult successMultiple() {
        return new DrawResult(Status.SUCCESS_MULTIPLE, null);
    }

    /**
     *  @return an error result indicating the deck is empty
     */
    public static DrawResult emptyDeck() {
        return new DrawResult(Status.EMPTY_DECK, null);
    }

    /**
     * @return an error result indicating the discard pile is empty
     */
    public static DrawResult emptyDiscard() {
        return new DrawResult(Status.EMPTY_DISCARD, null);
    }

    /** 
     * @return an error result indicating the player already drew 
     */
    public static DrawResult alreadyDrawn() {
        return new DrawResult(Status.ALREADY_DRAWN, null);
    }

    /**
     *  @return the status of the operation 
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
