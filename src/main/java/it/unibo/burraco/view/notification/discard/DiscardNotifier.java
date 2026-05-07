package it.unibo.burraco.view.notification.discard;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;

/**
 * Defines the contract for notifying the user of discard action errors.
 */
public interface DiscardNotifier {

    /**
     * Displays an error message corresponding to the given discard failure status.
     *
     * @param status the status describing why the discard failed
     */
    void notifyDiscardError(DiscardResult.Status status);
}
