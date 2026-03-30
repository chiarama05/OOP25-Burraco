package it.unibo.burraco.controller.closure;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.game.GameNotifier;
import it.unibo.burraco.controller.score.ScoreController;

public class ClosureManager {

    private final Turn turnModel;
    private final GameNotifier notifier;
    private final int targetScore;
    private final ScoreController scoreController;
    
    public ClosureManager(final Turn turnModel, final GameNotifier notifier, final int targetScore, final ScoreController scoreController) { 
        this.turnModel=turnModel;
        this.notifier = notifier;
        this.targetScore= targetScore;
        this.scoreController= scoreController; 
    }

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

    public void attemptClosure() {
        final Player current = turnModel.getCurrentPlayer();
        this.handleStateAfterDiscard(current);
    }

    private void triggerRoundEnd() {
        this.turnModel.setGameFinished(true);
        this.scoreController.onRoundEnd();
    }
}