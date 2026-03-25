package it.unibo.burraco.controller.discardcard;

import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.discard.TakeDiscardActionView;
import it.unibo.burraco.controller.drawcard.DrawResult;

public class TakeDiscardActionController {

    private final TakeDiscardController takeDiscardController;
    private final Turn turnModel;
    private final DiscardPile discardPile;

    public TakeDiscardActionController(TakeDiscardController takeDiscardController,
                                        Turn turnModel,
                                        DiscardPile discardPile) {
        this.takeDiscardController = takeDiscardController;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
    }

    public void handle(TakeDiscardActionView view) {
        DrawResult result = takeDiscardController.tryTakeDiscard();

        switch (result.getStatus()) {
            case SUCCESS_MULTIPLE -> {
                Player current = turnModel.getCurrentPlayer();
                view.onTakeDiscardSuccess(current, discardPile.getCards());
            }
            case ALREADY_DRAWN -> view.onTakeDiscardError("already_drawn");
            case EMPTY_DISCARD -> view.onTakeDiscardError("empty_discard");
            default -> {}
        }
    }
}