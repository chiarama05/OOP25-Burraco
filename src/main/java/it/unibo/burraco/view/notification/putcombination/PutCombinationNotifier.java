package it.unibo.burraco.view.notification.putcombination;

import it.unibo.burraco.controller.combination.putcombination.PutCombinationResult;

public interface PutCombinationNotifier {
  
    void notifyCombinationError(PutCombinationResult result);
}
