package it.unibo.burraco.controller.deck;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.view.deck.DeckDrawView;
import it.unibo.burraco.view.notification.deck.DeckNotifier;

public class DeckActionController {

    private final GameController gameController;
    private final DrawManager drawManager;
    private final DeckNotifier notifier;

    public DeckActionController(GameController gameController, DrawManager drawManager, DeckNotifier notifier) {
        this.gameController = gameController;
        this.drawManager = drawManager;
        this.notifier = notifier;
    }

    public void handle(DeckDrawView view) {
    var currentPlayer = gameController.getCurrentPlayer();
    var deck = gameController.getCommonDeck();

    DrawResult result = drawManager.drawFromDeck(currentPlayer, deck);

    if (result.getStatus() == DrawResult.Status.SUCCESS) {
        view.onDrawSuccess(currentPlayer, currentPlayer.getHand());
    } else {
        notifier.notifyDrawError(result);
    }
}
}