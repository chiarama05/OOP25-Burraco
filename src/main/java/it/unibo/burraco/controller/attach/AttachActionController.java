package it.unibo.burraco.controller.attach;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.attach.AttachView;
import it.unibo.burraco.view.notification.attach.AttachNotifier;
import java.util.List;

/**
 * Controller responsible for handling a card attachment action.
 * Orchestrates attach logic, sound, pot, and closure management
 * based on the result returned by {@link AttachController}.
 */
public class AttachActionController {

    private final GameController gameController;
    private final AttachController attachController;
    private final PotManager potManager;
    private final ClosureManager closureManager;
    private final AttachNotifier attachNotifier;
    private final boolean isPlayer1Owner;

    /**
     * Constructs the controller and retrieves the {@link AttachController}
     * from the provided {@link GameController}.
     *
     * @param gameController  the main game controller
     * @param potManager      the manager handling pot logic
     * @param closureManager  the manager handling game closure logic
     * @param attachNotifier  the notifier used to report attach errors to the user
     * @param isPlayer1Owner  true if this controller manages Player 1's combinations
     */
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

    /**
     * Handles an attach attempt triggered by the player.
     * Verifies ownership and draw status, delegates to {@link AttachController#tryAttach},
     * then dispatches the result to the view or notifier accordingly.
     *
     * @param selectedCards    the cards the player wants to attach
     * @param combinationCards the existing combination to attach to
     * @param view             the view to update on success
     */
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
                ATTACH_FAILED -> attachNotifier.notifyAttachError(result);

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