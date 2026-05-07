package it.unibo.burraco.controller.closure;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.closure.ClosureNotifier;
import it.unibo.burraco.controller.score.ScoreController;

/**
 * Orchestrates the end-of-round logic triggered by a player action or discard.
 * Evaluates the current ClosureState of a player and reacts accordingly:
 * it may notify the user of a constraint violation, or trigger the end of the round
 * by delegating to ScoreController.
 */
public class ClosureManager {

    private final Turn turnModel;
    private final ClosureNotifier notifier;
    private final ScoreController scoreController;
    private final ClosureValidator validator;

    /**
     * Constructs a ClosureManager with all required collaborators.
     *
     * @param turnModel       the model tracking the current turn and game state
     * @param notifier        the notifier used to display game messages to the players
     * @param targetScore     the score threshold that ends the entire match
     * @param scoreController the controller invoked when the round ends
     */
    public ClosureManager(
            final Turn turnModel,
            final ClosureNotifier notifier,
            final int targetScore,
            final ScoreController scoreController) {
        this.turnModel = turnModel;
        this.notifier = notifier;
        this.scoreController = scoreController;
        this.validator = new ClosureValidator();
    }

    /**
     * Evaluates the game state after a generic action (put combination or attach)
     * and reacts to constraint violations or valid closure conditions.
     *
     * @param player the player who just performed the action
     * @return true if the normal turn flow must be interrupted, false otherwise
     */
    public boolean handleStateAfterAction(final Player player) {
        final ClosureState state = this.validator.evaluate(player);

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
     *
     * @param player the player who just discarded
     * @return true if the round ended, false otherwise
     */
    public boolean handleStateAfterDiscard(final Player player) {
        final ClosureState state = this.validator.evaluateAfterDiscard(player);

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
     * and delegates to handleStateAfterDiscard.
     * Intended to be called directly from UI action handlers.
     */
    public void attemptClosure() {
        final Player current = this.turnModel.getCurrentPlayer();
        this.handleStateAfterDiscard(current);
    }

    private void triggerRoundEnd() {
        this.turnModel.setGameFinished(true);
        this.scoreController.onRoundEnd();
    }
}
