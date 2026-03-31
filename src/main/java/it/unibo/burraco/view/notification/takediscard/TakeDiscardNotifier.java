package it.unibo.burraco.view.notification.takediscard;

import it.unibo.burraco.controller.drawcard.DrawResult;

/**
 * Interface responsible for notifying the user about errors encountered
 * when trying to pick up the entire discard pile.
 */
@FunctionalInterface
public interface TakeDiscardNotifier {

    /**
     * Notifies the user of an error during the take-discard action.
     *
     * @param result the {@link DrawResult} containing the error status
     */
    void notifyTakeDiscardError(final DrawResult result);

}
