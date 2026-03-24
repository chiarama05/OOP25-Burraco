package it.unibo.burraco.controller.buttonLogic;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.discardcard.DiscardManagerImpl;
import it.unibo.burraco.controller.discardcard.DiscardResult;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;

public class DiscardController {
    private final DiscardManagerImpl discardManager;
    private final TurnController turnCtrl;
    private final PotManager potCtrl;
    private final ClosureManager closureCtrl;

    public DiscardController(DiscardManagerImpl discardManager, TurnController turnCtrl, PotManager potCtrl, ClosureManager closureCtrl) {
        this.discardManager = discardManager;
        this.turnCtrl = turnCtrl;
        this.potCtrl = potCtrl;
        this.closureCtrl = closureCtrl;
    }

   
    public DiscardResult executeDiscard(Player current, Card card) {
        
        boolean willTakePot = (current.getHand().size() == 1 && !current.isInPot());
        
        DiscardResult result = discardManager.discard(current, card);



        // DiscardManagerImpl already undid the discard
        if (!result.isValid()) {
            return result;
        }


         // The model says the round is over (pot taken + burraco + last card discarded).
        // ClosureManager shows ScoreView and does NOT advance the turn.
        if (result.isGameWon()) {
            closureCtrl.handleStateAfterDiscard(current);
            return result;
        }

        // Normal discard path
        if (willTakePot) {
            potCtrl.handlePot(true);
        }
        turnCtrl.executeNextTurn();

        return result;



        // ##
        /*if (result.isValid()) {
            if (result.isGameWon()) {
                closureCtrl.attemptClosure();
            } else {
                if (willTakePot) {
                    potCtrl.handlePot(true);
                }
                turnCtrl.executeNextTurn();
            }
        }
        return result;*/
    }
}