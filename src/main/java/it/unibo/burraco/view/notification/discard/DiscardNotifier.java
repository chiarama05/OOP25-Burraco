package it.unibo.burraco.view.notification.discard;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;

/**
 * Interface responsible for notifying the user about errors
 * that occur during the card discarding process.
 */
@FunctionalInterface
public interface DiscardNotifier {

    /**
     * Notifies the user of an error during discard.
     *
     * @param result the result object containing the error message/code
     */
    void notifyDiscardError(DiscardResult result);

}
