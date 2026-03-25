package it.unibo.burraco.view.notification.putcombination;

import it.unibo.burraco.controller.combination.PutCombinationResult;

public interface PutCombinationNotifier {
  
    void notifyCombinationError(PutCombinationResult result);
}
