package it.unibo.burraco.view.table;

/**
 * View-layer enum describing the action the player wants to perform.
 * The view never needs to import {@link it.unibo.burraco.model.move.Move}
 * or {@link it.unibo.burraco.model.move.Move.Type} — it uses this enum instead.
 * The Controller is responsible for translating a {@link ViewAction} into a {@code Move}.
 */
public enum PlayerAction {
    /** Draw a card from the deck. */
    DRAW_DECK,
    /** Take all cards from the discard pile. */
    DRAW_DISCARD,
    /** Place a new combination on the table. */
    PUT_COMBINATION,
    /** Attach selected cards to an existing combination. */
    ATTACH,
    /** Discard a card to end the turn. */
    DISCARD
}
