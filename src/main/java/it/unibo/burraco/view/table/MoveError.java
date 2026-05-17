package it.unibo.burraco.view.table;

/**
 * View-layer enum describing why a move was rejected by the model.
 * The view never needs to import {@code MoveResult}.
 */
public enum MoveError {
    /** The player tried to draw a second time in the same turn. */
    ALREADY_DRAWN,
    /** The player tried to play cards without drawing first. */
    NOT_DRAWN,
    /** The player confirmed an action with no cards selected. */
    NO_CARDS_SELECTED,
    /** The selected cards do not form a valid combination. */
    INVALID_COMBINATION,
    /** The move would leave the player unable to discard legally. */
    WOULD_GET_STUCK,
    /** The player tried to attach to another player's combination. */
    WRONG_PLAYER,
    /** A catch-all for unexpected error states. */
    UNKNOWN
}
