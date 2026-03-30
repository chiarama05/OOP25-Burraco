package it.unibo.burraco.view.notification.attach;

import it.unibo.burraco.controller.attach.AttachResult;

/**
 * Interface representing a notifier for card attachment actions.
 * It provides methods to alert the user when an attachment fails.
 */
public interface AttachNotifier {

    /**
     * Notifies the user of an error during the attach process.
     * 
     * @param result the result of the attachment attempt containing the error type
     */
    void notifyAttachError(final AttachResult result);

}
