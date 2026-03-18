package core.closure;

import model.player.Player;
import model.player.PlayerImpl;
import model.turn.Turn;
import view.notification.GameNotifier;
import view.table.TableView;
import view.table.TableViewImpl;
import view.score.ScoreView;
import view.score.ScoreViewImpl;

public class ClosureManager {

    private final Turn turnModel;
    private final TableView view;
    private final GameNotifier notifier;
    private final int targetScore;
    

    public ClosureManager(Turn turnModel, TableView view, GameNotifier notifier,int targetScore) {
        this.turnModel = turnModel;
        this.view = view;
        this.notifier = notifier;
        this.targetScore = targetScore;
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

        // ##
        /*if (model.canClose() && current.getHand().isEmpty()) {
            model.setGameFinished(true);
            
            if (view instanceof TableViewImpl tvImpl) {
                ScoreViewImpl scorePanel = new ScoreViewImpl(
                    model.getPlayer1(), 
                    model.getPlayer2(), 
                    model.getPlayer1().getName(), 
                    model.getPlayer2().getName(), 
                    targetScore, 
                    tvImpl, 
                    tvImpl.getGameController()
                );
                scorePanel.display(); 
            }
            
        } else {
            notifier.notifyInvalidClosure(); 
        }*/
    }


    private void triggerRoundEnd() {
        turnModel.setGameFinished(true);

        if (view instanceof TableViewImpl tvImpl) {
            ScoreViewImpl scorePanel = new ScoreViewImpl(turnModel.getPlayer1(),turnModel.getPlayer2(),turnModel.getPlayer1().getName(),turnModel.getPlayer2().getName(),targetScore,tvImpl,tvImpl.getGameController());
            scorePanel.display();
        }
    }
}

