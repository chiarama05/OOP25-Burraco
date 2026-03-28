package it.unibo.burraco.controller.attach;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.attach.AttachView;
import it.unibo.burraco.view.notification.attach.AttachNotifier;

import java.util.List;

public class AttachActionController {

    private final GameController gameController;
    private final AttachController attachController;
    private final PotManager potManager;
    private final ClosureManager closureManager;
    private final AttachNotifier attachNotifier;
    private final boolean isPlayer1Owner;

    public AttachActionController(GameController gameController,
                                   PotManager potManager,
                                   ClosureManager closureManager,
                                   AttachNotifier attachNotifier,
                                   boolean isPlayer1Owner) {
        this.gameController = gameController;
        this.attachController = gameController.getAttachController();
        this.potManager = potManager;
        this.closureManager = closureManager;
        this.attachNotifier = attachNotifier;
        this.isPlayer1Owner = isPlayer1Owner;
    }

    public void handle(List<Card> selectedCards, List<Card> combinationCards, AttachView view) {
    Player currentPlayer = gameController.getCurrentPlayer();
    boolean hasDrawn = gameController.getDrawManager().hasDrawn();
    boolean isPlayer1Current = gameController.isPlayer1(currentPlayer); 
    boolean isCurrentPlayer = isPlayer1Current == isPlayer1Owner;

    AttachResult result = attachController.tryAttach(
            currentPlayer, selectedCards, combinationCards, hasDrawn, isCurrentPlayer);

    if (result == AttachResult.SUCCESS_BURRACO) {
        gameController.getSoundController().playBurracoSound();
    }

    switch (result) {
        case NOT_DRAWN,
             WRONG_PLAYER,
             NO_CARDS_SELECTED,
             INVALID_COMBINATION,
             WOULD_GET_STUCK,
             ATTACH_FAILED ->
            attachNotifier.notifyAttachError(result);

        case SUCCESS, SUCCESS_BURRACO -> {
            view.updateCombinationVisuals();
            view.onAttachSuccess(currentPlayer, isPlayer1Current); 
        }

        case SUCCESS_TAKE_POT -> {
            view.updateCombinationVisuals();
            potManager.handlePot(false);
            view.onAttachTakePot(currentPlayer, isPlayer1Current); 
        }

        case SUCCESS_CLOSE, SUCCESS_STUCK -> {
            view.updateCombinationVisuals();
            closureManager.handleStateAfterAction(currentPlayer);
            view.onAttachClose(currentPlayer, isPlayer1Current);
        }
    }
}
}