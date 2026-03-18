package core.buttonLogic;

import model.card.Card;
import model.player.Player;
import core.pot.PotManager;
import core.turn.TurnController;
import core.closure.ClosureManager;
import core.discardcard.*;

public class DiscardController {
    private final DiscardManagerImpl discardManager;
    private final TurnController turnCtrl;
    private final PotManager potCtrl;
    private final ClosureManager closureCtrl;

    public DiscardController(DiscardManagerImpl discardManager, TurnController turnCtrl, 
                             PotManager potCtrl, ClosureManager closureCtrl) {
        this.discardManager = discardManager;
        this.turnCtrl = turnCtrl;
        this.potCtrl = potCtrl;
        this.closureCtrl = closureCtrl;
    }

   
    public DiscardResult executeDiscard(Player current, Card card) {
        
        boolean willTakePot = (current.getHand().size() == 1 && !current.isInPot());
        
        DiscardResult result = discardManager.discard(current, card);

        if (result.isValid()) {
            if (result.isGameWon()) {
                closureCtrl.attemptClosure();
            } else {
                if (willTakePot) {
                    potCtrl.handlePot(true);
                }
                turnCtrl.executeNextTurn();
            }
        }
        return result;
    }
}