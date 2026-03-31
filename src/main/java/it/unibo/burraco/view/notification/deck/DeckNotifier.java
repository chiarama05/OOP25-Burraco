package it.unibo.burraco.view.notification.deck;

import it.unibo.burraco.controller.drawcard.DrawResult;

/**
 * Interface for notifying errors or issues related to drawing cards from the deck.
 */
@FunctionalInterface
public interface DeckNotifier {

    /**
     * Notifies the user of an error during the deck drawing process.
     * 
     * @param result the result of the draw operation containing the error status
     */
    void notifyDrawError(DrawResult result);

}
