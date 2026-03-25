
package it.unibo.burraco.controller.discardcard;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

import java.util.Set;

public class DiscardController {

    private final DiscardManagerImpl discardManager;
    private final TurnController turnCtrl;
    private final PotManager potCtrl;
    private final ClosureManager closureCtrl;
    private final DrawManager drawManager;  
    private final Turn turnModel;          

    public DiscardController(DiscardManagerImpl discardManager,
                              TurnController turnCtrl,
                              PotManager potCtrl,
                              ClosureManager closureCtrl,
                              DrawManager drawManager,
                              Turn turnModel) {
        this.discardManager = discardManager;
        this.turnCtrl = turnCtrl;
        this.potCtrl = potCtrl;
        this.closureCtrl = closureCtrl;
        this.drawManager = drawManager;
        this.turnModel = turnModel;
    }

    
    public DiscardResult tryDiscard(Set<Card> selectedCards) {

       
        if (!drawManager.hasDrawn()) {
            return DiscardResult.error("must_draw");
        }

        if (selectedCards.size() != 1) {
            return DiscardResult.error("select_one");
        }

        Player current = turnModel.getCurrentPlayer();
        Card card = selectedCards.iterator().next();

        boolean willTakePot = (current.getHand().size() == 1 && !current.isInPot());

        DiscardResult result = discardManager.discard(current, card);

        if (!result.isValid()) {
            return result;
        }

        if (result.isGameWon()) {
            closureCtrl.handleStateAfterDiscard(current);
            return DiscardResult.success(
                discardManager.getDiscardPile(), current, true);
        }

        if (willTakePot) {
            potCtrl.handlePot(true);
        }

        turnCtrl.executeNextTurn();

        return DiscardResult.success(
            discardManager.getDiscardPile(), current, false);
    }
}