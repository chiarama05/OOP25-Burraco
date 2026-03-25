package it.unibo.burraco.view.notification.attach;

import it.unibo.burraco.controller.attach.AttachResult;

public interface AttachNotifier {

    void notifyAttachError(AttachResult result);

}
