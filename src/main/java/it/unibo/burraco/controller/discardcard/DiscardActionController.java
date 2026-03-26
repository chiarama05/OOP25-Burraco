package it.unibo.burraco.controller.discardcard;

import it.unibo.burraco.view.discard.DiscardActionView;

public class DiscardActionController {

    private final DiscardController discardController;

    public DiscardActionController(DiscardController discardController) {
        this.discardController = discardController;
    }

    public void handle(DiscardActionView view, boolean isPlayer1) {
    DiscardResult result = discardController.tryDiscard(view.getSelectedCards(isPlayer1));

    if (result.isValid()) {
        view.onDiscardSuccess(result.getCurrentPlayer(), result.getUpdatedDiscardPile(), isPlayer1);
    } else {
        view.onDiscardError(result.getMessage());
    }
}
}
