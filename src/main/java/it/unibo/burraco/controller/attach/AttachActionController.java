package it.unibo.burraco.controller.attach;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.attach.AttachView;

import java.util.List;

public class AttachActionController {

    private final GameController gameController;
    private final AttachController attachController;
    private final PotManager potManager;
    private final ClosureManager closureManager;
    private final boolean isPlayer1Owner;

    public AttachActionController(GameController gameController,
                                   PotManager potManager,
                                   ClosureManager closureManager,
                                   boolean isPlayer1Owner) {
        this.gameController = gameController;
        this.attachController = gameController.getAttachController();
        this.potManager = potManager;
        this.closureManager = closureManager;
        this.isPlayer1Owner = isPlayer1Owner;
    }

    public void handle(List<Card> selectedCards, List<Card> combinationCards, AttachView view) {
        Player currentPlayer = gameController.getCurrentPlayer();
        boolean hasDrawn = gameController.getDrawManager().hasDrawn();
        boolean isCurrentPlayer = gameController.isPlayer1(currentPlayer) == isPlayer1Owner;

        AttachResult result = attachController.tryAttach(
                currentPlayer, selectedCards, combinationCards, hasDrawn, isCurrentPlayer);

        if (result == AttachResult.SUCCESS_BURRACO) {
            gameController.getSoundController().playBurracoSound();
        }

        switch (result) {
            case NOT_DRAWN ->
                view.showAttachError("You have to draw first!", "Error");

            case WRONG_PLAYER ->
                view.showAttachError("You can only attach cards to your own combinations!", "Error");

            case NO_CARDS_SELECTED ->
                view.showAttachError("Select the card from your hand first!", "Error");

            case INVALID_COMBINATION ->
                view.showAttachError(
                    "Invalid move: the resulting combination would not be valid!\n"
                    + "(wrong suit, too many wildcards, or broken sequence)",
                    "Move Not Allowed");

            case WOULD_GET_STUCK ->
                view.showAttachError(
                    "You cannot attach this card!\n\n"
                    + "After attaching you would have only 1 card left,\n"
                    + "but you don't have a Burraco yet and you cannot close.\n\n"
                    + "You need at least one Burraco before you can reduce\n"
                    + "your hand to 1 card.",
                    "Move Not Allowed");

            case ATTACH_FAILED ->
                view.showAttachError("These cards cannot be attached!", "Error");

            case SUCCESS, SUCCESS_BURRACO -> {
                view.updateCombinationVisuals();
                view.onAttachSuccess(currentPlayer);
            }

            case SUCCESS_TAKE_POT -> {
                view.updateCombinationVisuals();
                potManager.handlePot(false);
                view.onAttachTakePot(currentPlayer);
            }

            case SUCCESS_CLOSE, SUCCESS_STUCK -> {
                view.updateCombinationVisuals();
                closureManager.handleStateAfterAction(currentPlayer);
                view.onAttachClose(currentPlayer);
            }
        }
    }
}