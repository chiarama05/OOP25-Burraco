package it.unibo.burraco.controller.closure;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.GameNotifier;
import it.unibo.burraco.controller.score.ScoreController;

public class ClosureManager {

    private final Turn turnModel;
    private final GameNotifier notifier;
    private final int targetScore;
    private final ScoreController scoreController;
    

    public ClosureManager(Turn turnModel, GameNotifier notifier, int targetScore, ScoreController scoreController) { 
        this.turnModel        = turnModel;
        this.notifier         = notifier;
        this.targetScore      = targetScore;
        this.scoreController  = scoreController; 
    }



    public boolean handleStateAfterAction(Player player) {
        ClosureState state = ClosureValidator.evaluate(player);

        switch (state) {
            case OK:
                return false;

            // put combination emptied the hand but pot not yet taken.
            case ZERO_CARDS_NO_POT:
                notifier.notifyMustTakePotBeforeDiscard();
                return true;

            // pot taken, hand empty, no burraco. player MUST keep playing (cannot discard, cannot close).
            case ZERO_CARDS_NO_BURRACO:
                notifier.notifyMustFormBurracoBeforeClose();
                return true;  

            
            // pot taken, hand empty, burraco present. The round ends immediately (player 
            // already discarded everything – the "close on play" scenario.
            case CAN_CLOSE:
                triggerRoundEnd();
                return true;

            default:
                return false;
        }
    }


    public boolean handleStateAfterDiscard(Player player) {
        ClosureState state = ClosureValidator.evaluateAfterDiscard(player);

        switch (state) {
            case ROUND_WON:
                triggerRoundEnd();
                return true;

            //player tried to discard last card without a burraco.
            case CANNOT_CLOSE_NO_BURRACO:
                notifier.notifyInvalidClosure();
                return false;

            default:
                return false;
        }
    }

    public void attemptClosure() {

        Player current = turnModel.getCurrentPlayer();
        handleStateAfterDiscard(current);
    }


    private void triggerRoundEnd() {
        turnModel.setGameFinished(true);
        scoreController.onRoundEnd();
    }
}

