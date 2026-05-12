package it.unibo.burraco.model.rules;

/**
 * Represents every possible state the game can be in
 * with respect to the definitive closure.
 */
public enum ClosureState {

    /**
     * Everything is fine – the player can keep playing normally.
     */
    OK,

    /**
     * The player finished all hand cards but has NOT yet taken the pot.
     * The pot must be drawn on-the-fly.
     */
    MUST_TAKE_POT,

    /**
     * The player has taken the pot and has at least one Burraco.
     * They are now allowed to close by discarding their last card.
     */
    CAN_CLOSE,

    /**
     * The player would like to close but does not yet have a Burraco.
     * They MUST keep playing.
     */
    CANNOT_CLOSE_NO_BURRACO,

    /**
     * The player just played or attached cards and now has 0 cards left in hand
     * WITHOUT having taken the pot yet.
     * They cannot close and must take the pot before discarding.
     */
    ZERO_CARDS_NO_POT,

    /**
     * The player has 0 cards left in hand, has taken the pot, but has NOT yet formed a Burraco.
     * They cannot close and cannot discard – they must form a Burraco first.
     */
    ZERO_CARDS_NO_BURRACO,

    /**
     * The player discarded their last card after taking the pot and completing
     * at least one Burraco. The round is over.
     */
    ROUND_WON
}
