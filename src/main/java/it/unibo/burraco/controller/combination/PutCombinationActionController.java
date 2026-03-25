package it.unibo.burraco.controller.combination;

import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.combination.PutCombinationView;

import java.util.List;

public class PutCombinationActionController {

    private final GameController gameController;
    private final PutCombinationController putComboController;

    public PutCombinationActionController(GameController gameController,
                                           PutCombinationController putComboController) {
        this.gameController = gameController;
        this.putComboController = putComboController;
    }

    public void handle(List<Card> selected, PutCombinationView view) {
        Player current = gameController.getCurrentPlayer();

        PutCombinationResult result = putComboController.tryPutCombination(selected);

        switch (result.getStatus()) {

            case NOT_DRAWN ->
                view.showCombinationError("Draw a card first!", "Error");

            case NO_CARDS_SELECTED ->
                view.showCombinationError("Select cards from your hand first!", "Error");

            case WOULD_GET_STUCK ->
                view.showCombinationError(
                    "You cannot play this combination!\n\n"
                    + "After placing it you would have only 1 card left,\n"
                    + "but you don't have a Burraco yet and you cannot close.\n\n"
                    + "You need at least one Burraco before you can reduce\n"
                    + "your hand to 1 card.",
                    "Move Not Allowed");

            case INVALID_COMBINATION ->
                view.showCombinationError("Invalid combination or not enough cards!", "Error");

            case SUCCESS, SUCCESS_BURRACO ->
                view.onCombinationSuccess(result.getProcessedCombo(), result.isPlayer1(), current);

            case SUCCESS_TAKE_POT ->
                view.onCombinationTakePot(result.getProcessedCombo(), result.isPlayer1(), current);

            case SUCCESS_CLOSE, SUCCESS_STUCK ->
                view.onCombinationClose(result.getProcessedCombo(), result.isPlayer1(), current);
        }
    }
}
