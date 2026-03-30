package it.unibo.burraco.controller.attach;

/**
 * Represents all possible outcomes of an attach operation.
 */
public enum AttachResult {

    /** Card(s) attached successfully. */
    SUCCESS,

    /** Attach completed a Burraco (7+ card combination). */
    SUCCESS_BURRACO,

    /** Attach left the player with 0 cards — must take the pot. */
    SUCCESS_TAKE_POT,

    /** Attach triggered a valid game closure. */
    SUCCESS_CLOSE,

    /** Attach left the player with 0 cards but no Burraco. */
    SUCCESS_STUCK,

    /** Failed: player has not drawn yet this turn. */
    NOT_DRAWN,

    /** Failed: this combination belongs to the other player. */
    WRONG_PLAYER,

    /** Failed: no cards were selected. */
    NO_CARDS_SELECTED,

    /** Failed: resulting combination would be invalid. */
    INVALID_COMBINATION,

    /** Failed: player would be left with no valid moves. */
    WOULD_GET_STUCK,

    /** Failed: generic attach failure. */
    ATTACH_FAILED
}