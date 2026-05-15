package it.unibo.burraco.view.table;

/**
 * View-layer enum describing why a move was rejected.
 * The view never needs to import model.move.MoveResult.
 */
public enum MoveError {
    ALREADY_DRAWN,
    NOT_DRAWN,
    NO_CARDS_SELECTED,
    INVALID_COMBINATION,
    WOULD_GET_STUCK,
    WRONG_PLAYER,
    UNKNOWN
}