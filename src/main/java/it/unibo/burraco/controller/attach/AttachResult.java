package it.unibo.burraco.controller.attach;

/**
 * Represents all possible outcomes of an attach operation.
 */
public enum AttachResult {
    SUCCESS,              // Card(s) attached successfully
    SUCCESS_BURRACO,      // Attach completed a Burraco (7+ card combination)
    SUCCESS_TAKE_POT,     // Attach left the player with 0 cards — must take the pot
    SUCCESS_CLOSE,        // Attach triggered a valid game closure
    SUCCESS_STUCK,        // Attach left the player with 0 cards but no Burraco
    NOT_DRAWN,            // Failed: player has not drawn yet this turn
    WRONG_PLAYER,         // Failed: this combination belongs to the other player
    NO_CARDS_SELECTED,    // Failed: no cards were selected
    INVALID_COMBINATION,  // Failed: resulting combination would be invalid
    WOULD_GET_STUCK,      // Failed: player would be left with no valid moves
    ATTACH_FAILED         // Failed: generic attach failure
}
