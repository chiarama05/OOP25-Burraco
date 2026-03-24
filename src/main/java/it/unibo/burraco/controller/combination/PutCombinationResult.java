package it.unibo.burraco.controller.combination;

import it.unibo.burraco.model.card.Card;
import java.util.List;

public class PutCombinationResult {

    public enum Status {
        NOT_DRAWN,
        NO_CARDS_SELECTED,
        WOULD_GET_STUCK,
        INVALID_COMBINATION,
        SUCCESS,
        SUCCESS_BURRACO,
        SUCCESS_TAKE_POT,
        SUCCESS_CLOSE,
        SUCCESS_STUCK
    }

    private final Status status;
    private final List<Card> processedCombo; // null se fallito
    private final boolean isPlayer1;

    private PutCombinationResult(Status status, List<Card> combo, boolean isPlayer1) {
        this.status = status;
        this.processedCombo = combo;
        this.isPlayer1 = isPlayer1;
    }

    public static PutCombinationResult error(Status status) {
        return new PutCombinationResult(status, null, false);
    }

    public static PutCombinationResult success(Status status, List<Card> combo, boolean isPlayer1) {
        return new PutCombinationResult(status, combo, isPlayer1);
    }

    public Status getStatus()            { return status; }
    public List<Card> getProcessedCombo(){ return processedCombo; }
    public boolean isPlayer1()           { return isPlayer1; }
}
