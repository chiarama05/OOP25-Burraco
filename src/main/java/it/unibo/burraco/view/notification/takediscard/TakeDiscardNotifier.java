package it.unibo.burraco.view.notification.takediscard;

import it.unibo.burraco.controller.drawcard.DrawResult;

public interface TakeDiscardNotifier {

    void notifyTakeDiscardError(DrawResult result);

}
