package it.unibo.burraco.view.notification.discard;

import it.unibo.burraco.controller.discardcard.DiscardResult;

public interface DiscardNotifier {

    void notifyDiscardError(DiscardResult result);

}
