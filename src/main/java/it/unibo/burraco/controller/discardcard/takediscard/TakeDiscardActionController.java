package it.unibo.burraco.controller.discardcard.takediscard;

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

    if (result.getStatus() == DrawResult.Status.SUCCESS_MULTIPLE) {
        Player current = turnModel.getCurrentPlayer();
        boolean isP1 = turnModel.isPlayer1Turn(); 

        view.onTakeDiscardSuccess(current, discardPile.getCards(), isP1);
    } else {
        view.onTakeDiscardError(result);
    }
}
}