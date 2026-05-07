package it.unibo.burraco.view.notification.closure;

/**
 * Notifies the user about game closure constraint violations.
 * Used by ClosureManager to communicate illegal game states.
 */
public interface ClosureNotifier {

    /**
     * Notifies that the closure attempt was invalid due to missing Burraco.
     */
    void notifyInvalidClosure();

    /**
     * Notifies that the pot must be taken before the player can discard.
     */
    void notifyMustTakePotBeforeDiscard();

    /**
     * Notifies that a Burraco is required to close the round.
     */
    void notifyMustFormBurracoBeforeClose();
}
