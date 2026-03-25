package it.unibo.burraco.controller.combination;

import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.combination.PutCombinationView;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifier;

import java.util.List;

public class PutCombinationActionController {

    private final GameController gameController;
    private final PutCombinationController putComboController;
    private final PutCombinationNotifier notifier; 

    public PutCombinationActionController(GameController gameController,
                                           PutCombinationController putComboController,
                                           PutCombinationNotifier notifier) { 
        this.gameController = gameController;
        this.putComboController = putComboController;
        this.notifier = notifier;
    }

    public void handle(List<Card> selected, PutCombinationView view) {
        Player current = gameController.getCurrentPlayer();

        PutCombinationResult result = putComboController.tryPutCombination(selected);
        PutCombinationResult.Status status = result.getStatus();


        if (isError(status)) {
            notifier.notifyCombinationError(result);
            return; 
        }


        switch (status) {
            case SUCCESS, SUCCESS_BURRACO ->
                view.onCombinationSuccess(result.getProcessedCombo(), result.isPlayer1(), current);

            case SUCCESS_TAKE_POT ->
                view.onCombinationTakePot(result.getProcessedCombo(), result.isPlayer1(), current);

            case SUCCESS_CLOSE, SUCCESS_STUCK ->
                view.onCombinationClose(result.getProcessedCombo(), result.isPlayer1(), current);
            
            default -> {
                
            }
        }
    }


    private boolean isError(PutCombinationResult.Status status) {
        return status == PutCombinationResult.Status.NOT_DRAWN ||
               status == PutCombinationResult.Status.NO_CARDS_SELECTED ||
               status == PutCombinationResult.Status.WOULD_GET_STUCK ||
               status == PutCombinationResult.Status.INVALID_COMBINATION;
    }
}