package it.unibo.burraco.controller.combination.putcombination;

import java.util.List;

import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.combination.PutCombinationView;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifier;

/**
 * Controller responsible for handling the action of putting a new combination on the table.
 * It acts as an orchestrator between the game logic and the view/notification layers.
 */
public class PutCombinationActionController {

    private final GameController gameController;
    private final PutCombinationController putComboController;
    private final PutCombinationNotifier notifier; 

    /**
     * Constructor for PutCombinationActionController.
     * 
     * @param gameController      the main game controller to access general game state
     * @param putComboController the controller that handles the business logic of combinations
     * @param notifier            the component responsible for notifying users about errors
     */
    public PutCombinationActionController(final GameController gameController,
                                           final PutCombinationController putComboController,
                                           final PutCombinationNotifier notifier) { 
        this.gameController = gameController;
        this.putComboController = putComboController;
        this.notifier = notifier;
    }

    /**
     * Handles the request to lay down a set of cards.
     * It requests a validation and execution of the move, then updates the view accordingly.
     * 
     * @param selected the list of cards selected by the player
     * @param view     the view component that will reflect the changes in the UI
     * @implNote The current player is retrieved from the game state and passed
     *           to the view callbacks to identify which player performed the action.
     */
    public void handle(List<Card> selected, PutCombinationView view) {
        final Player current = gameController.getCurrentPlayer();
        final PutCombinationResult result = putComboController.tryPutCombination(selected);
        final PutCombinationResult.Status status = result.getStatus();

        // Check if the result represents a failure/error state
        if (isError(status)) {
            notifier.notifyCombinationError(result);
            return; 
        }

        // Handle success scenarios by triggering the appropriate view update
        switch (status) {
            case SUCCESS, SUCCESS_BURRACO ->
                // Standard success or a successful creation of a Burraco
                view.onCombinationSuccess(result.getProcessedCombo(), result.isPlayer1(), current);

            case SUCCESS_TAKE_POT ->
                // Move was successful and allowed the player to take the Pot
                view.onCombinationTakePot(result.getProcessedCombo(), result.isPlayer1(), current);

            case SUCCESS_CLOSE, SUCCESS_STUCK ->
                // Move was successful and led to closing the round/game
                view.onCombinationClose(result.getProcessedCombo(), result.isPlayer1(), current);
            
            default -> {
    
            }
        }
    }

    /**
     * Helper method to identify if a status belongs to a set of predefined error conditions.
     * 
     * @param status the status to check
     * @return true if the status is an error, false otherwise
     */
    private boolean isError(PutCombinationResult.Status status) {
        return status == PutCombinationResult.Status.NOT_DRAWN ||
               status == PutCombinationResult.Status.NO_CARDS_SELECTED ||
               status == PutCombinationResult.Status.WOULD_GET_STUCK ||
               status == PutCombinationResult.Status.INVALID_COMBINATION;
    }
}