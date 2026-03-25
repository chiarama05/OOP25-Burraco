package it.unibo.burraco.view.notification.deck;

import it.unibo.burraco.controller.drawcard.DrawResult;

public interface DeckNotifier {

    void notifyDrawError(DrawResult result);

}
