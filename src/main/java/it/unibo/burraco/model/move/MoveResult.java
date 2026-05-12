package it.unibo.burraco.model.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.burraco.model.cards.Card;

public final class MoveResult {

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

    public static MoveResult ok() {
        return new MoveResult(Status.SUCCESS, Collections.emptyList(), false);
    }

    public static MoveResult error(final Status s) {
        return new MoveResult(s, Collections.emptyList(), false);
    }

    public static MoveResult success(final Status s, final List<Card> cards, final boolean p1) {
        return new MoveResult(s, cards, p1);
    }

    public boolean isValid() {
        return status == Status.SUCCESS
            || status == Status.SUCCESS_BURRACO
            || status == Status.SUCCESS_TAKE_POT
            || status == Status.SUCCESS_CLOSE
            || status == Status.SUCCESS_STUCK
            || status == Status.ROUND_WON;
    }

    public Status getStatus() { return status; }

    public List<Card> getProcessedCards() {
        return Collections.unmodifiableList(processedCards);
    }

    public boolean isPlayer1() { 
        return player1; 
    }
}
