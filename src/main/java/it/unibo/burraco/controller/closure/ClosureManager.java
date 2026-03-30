package it.unibo.burraco.controller.closure;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.game.GameNotifier;
import it.unibo.burraco.controller.score.ScoreController;



/**
 * Orchestrates the end-of-round logic triggered by a player action or discard.
 * Evaluates the current {@link ClosureState} of a player and reacts accordingly:
 * it may notify the user of a constraint violation, or trigger the end of the round
 * by delegating to {@link ScoreController}.
 */
public class ClosureManager {

    private final Turn turnModel;
    private final GameNotifier notifier;
    private final int targetScore;
    private final ScoreController scoreController;
    

    /**
    * Constructs a ClosureManager with all required collaborators.
    * @param turnModel       the model tracking the current turn and game state
    * @param notifier        the notifier used to display game messages to the players
    * @param targetScore     the score threshold that ends the entire match
    * @param scoreController the controller invoked when the round ends
    */
   
    public ClosureManager(final Turn turnModel, final GameNotifier notifier, final int targetScore, final ScoreController scoreController) { 
        this.turnModel=turnModel;
        this.notifier = notifier;
        this.targetScore= targetScore;
        this.scoreController= scoreController; 
    }


    /**
    * Evaluates the game state after a generic action (put combination or attach)
    * and reacts to constraint violations or valid closure conditions.
    * Returns {@code true} if the turn flow must be interrupted (either because the player
    * must do something before discarding, or because the round has ended).
    * @param player the player who just performed the action
    * @return {@code true} if the normal turn flow must be interrupted, {@code false} otherwise
    */
    public boolean handleStateAfterAction(final Player player) {
        final ClosureState state = ClosureValidator.evaluate(player);

        switch (state) {
            case OK:
                return false;
            case ZERO_CARDS_NO_POT:
                this.notifier.notifyMustTakePotBeforeDiscard();
                return true;
            case ZERO_CARDS_NO_BURRACO:
                this.notifier.notifyMustFormBurracoBeforeClose();
                return true;  
            case CAN_CLOSE:
                this.triggerRoundEnd();
                return true;
            default:
                return false;
        }
    }

    /**
    * Evaluates the game state after a discard action and triggers the end of the
    * round if the conditions for a valid closure are met.
    * Returns {@code true} only when the round actually ends ({@link ClosureState#ROUND_WON}).
    * Returns {@code false} for all other states, including invalid closure attempts.
     * @param player the player who just discarded
     * @return {@code true} if the round ended, {@code false} otherwise
     */
    public boolean handleStateAfterDiscard(final Player player) {
        final ClosureState state = ClosureValidator.evaluateAfterDiscard(player);

        switch (state) {
            case ROUND_WON:
                this.triggerRoundEnd();
                return true;
            case CANNOT_CLOSE_NO_BURRACO:
                this.notifier.notifyInvalidClosure();
                return false;
            default:
                return false;
        }
    }

    /**
    * Convenience method that retrieves the current player from the turn model
    * and delegates to {@link #handleStateAfterDiscard(Player)}.
    * Intended to be called directly from UI action handlers.
    */
    public void attemptClosure() {
        final Player current = turnModel.getCurrentPlayer();
        this.handleStateAfterDiscard(current);
    }


    /**
    * Method 
    * 
    * 
    */
    private void triggerRoundEnd() {
        this.turnModel.setGameFinished(true);
        this.scoreController.onRoundEnd();
    }
}