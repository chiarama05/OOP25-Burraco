package it.unibo.burraco.controller.drawcard;

import it.unibo.burraco.model.card.Card;

/**
 * Immutable object representing the outcome of a draw operation.
 * It provides status information and the drawn card data to the caller.
 */
public class DrawResult {

    /**
     * Enumerates all possible results of a draw attempt.
     */
    public enum Status {
        SUCCESS,             // Single card drawn from deck
        SUCCESS_MULTIPLE,    // Multiple cards taken from discard pile
        EMPTY_DECK,          // Failed: Deck has no cards
        EMPTY_DISCARD,       // Failed: Discard pile is empty
        ALREADY_DRAWN        // Failed: Player already drew this turn
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
     * Private constructor to maintain control over instance creation via factory methods.
     */
    private DrawResult(Status status, Card card) {
        this.status = status;
        this.drawnCard = card;
    }

    // --- Static Factory Methods ---

    public static DrawResult success(Card card) {
        return new DrawResult(Status.SUCCESS, card);
    }

    public static DrawResult successMultiple() {
        return new DrawResult(Status.SUCCESS_MULTIPLE, null);
    }

    public static DrawResult emptyDeck() {
        return new DrawResult(Status.EMPTY_DECK, null);
    }

    public static DrawResult emptyDiscard() {
        return new DrawResult(Status.EMPTY_DISCARD, null);
    }

    public static DrawResult alreadyDrawn() {
        return new DrawResult(Status.ALREADY_DRAWN, null);
    }

    // --- Getters ---

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
