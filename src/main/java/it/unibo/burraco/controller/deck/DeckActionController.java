package it.unibo.burraco.controller.deck;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.view.deck.DeckDrawView;

public class DeckActionController {

    private final GameController gameController;
    private final DrawManager drawManager;

    public DeckActionController(GameController gameController, DrawManager drawManager) {
        this.gameController = gameController;
        this.drawManager = drawManager;
    }

    public void handle(DeckDrawView view) {
        var currentPlayer = gameController.getCurrentPlayer();
        var deck = gameController.getCommonDeck();

        DrawResult result = drawManager.drawFromDeck(currentPlayer, deck);

        switch (result.getStatus()) {
            case SUCCESS ->
                view.onDrawSuccess(currentPlayer);
            case ALREADY_DRAWN ->
                view.showDrawError("You have already drawn a card this turn!");
            case EMPTY_DECK ->
                view.showDrawError("The deck is empty!");
            default -> {}
        }
    }
}