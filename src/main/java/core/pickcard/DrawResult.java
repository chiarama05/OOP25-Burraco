package core.pickcard;

import card.Card;

public class DrawResult {

    public enum Status {
        SUCCESS,
        SUCCESS_MULTIPLE,
        EMPTY_DECK,
        EMPTY_DISCARD,
        ALREADY_DRAWN
    }

    private Status status;
    private Card drawnCard;

    private DrawResult(Status status, Card card) {
        this.status = status;
        this.drawnCard = card;
    }

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

    public Status getStatus() {
        return status;
    }

    public Card getDrawnCard() {
        return drawnCard;
    }

}
