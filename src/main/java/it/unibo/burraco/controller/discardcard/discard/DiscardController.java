package it.unibo.burraco.controller.discardcard.discard;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

import java.util.Set;

/**
 * Main logic controller for the discard action.
 * Verifies game rules (drawing before discarding) and coordinates
 * pot collection, turn changes, and game closure.
 */
public class DiscardController {

    private final DiscardManagerImpl discardManager;
    private final TurnController turnCtrl;
    private final PotManager potCtrl;
    private final ClosureManager closureCtrl;
    private final DrawManager drawManager;  
    private final Turn turnModel;          


    /**
     * Constructs a DiscardController with all its required collaborators.
     *
     * @param discardManager the manager that performs the actual card transfer
     * @param turnCtrl       the controller responsible for advancing the turn
     * @param potCtrl        the manager that handles automatic pot collection
     * @param closureCtrl    the manager that evaluates and triggers round closure
     * @param drawManager    used to verify that the player has drawn before discarding
     * @param turnModel      provides the current player reference
     */
    public DiscardController(
            final DiscardManagerImpl discardManager,
            final TurnController turnCtrl,
            final PotManager potCtrl, 
            final ClosureManager closureCtrl, 
            final DrawManager drawManager, 
            final Turn turnModel) {
        this.discardManager = discardManager;
        this.turnCtrl= turnCtrl;
        this.potCtrl = potCtrl;
        this.closureCtrl =closureCtrl;
        this.drawManager = drawManager;
        this.turnModel = turnModel;
    }

    /**
     * Validates and executes a discard action.
     * 
     * @param selectedCards the card selected by the player to be discarded.
     * @return a DiscardResult containing the outcome of the operation.
     */
    public DiscardResult tryDiscard(final Set<Card> selectedCards) {

        // A player must draw from the deck or pile before discarding
        if (!this.drawManager.hasDrawn()) {
            return DiscardResult.error("must_draw");
        }

        // Exactly one card must be discarded
        if (selectedCards.size() != 1) {
            return DiscardResult.error("select_one");
        }

        final Player current = this.turnModel.getCurrentPlayer();
        final Card card = selectedCards.iterator().next();
        final boolean willTakePot = (current.getHand().size() == 1 && !current.isInPot());
        final DiscardResult result = discardManager.discard(current, card);

        if (!result.isValid()) {
            return result;
        }
        if (result.isGameWon()) {
            this.closureCtrl.handleStateAfterDiscard(current);
            return DiscardResult.success(this.discardManager.getDiscardPile(), current, true);
        }
        if (willTakePot) {
            this.potCtrl.handlePot(true);
        }
        this.turnCtrl.executeNextTurn();

        return DiscardResult.success(this.discardManager.getDiscardPile(), current, false);
    }
}
