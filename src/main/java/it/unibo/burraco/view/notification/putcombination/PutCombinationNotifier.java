package it.unibo.burraco.view.notification.putcombination;

import it.unibo.burraco.controller.combination.putcombination.PutCombinationResult;

/**
 * Interface for notifying errors or issues encountered when a player 
 * attempts to place a new combination on the table.
 */
public interface PutCombinationNotifier {
  
    /**
     * Notifies the user about an error during the combination placement.
     * 
     * @param result the {@link PutCombinationResult} containing the error status
     */
    void notifyCombinationError(PutCombinationResult result);
}
