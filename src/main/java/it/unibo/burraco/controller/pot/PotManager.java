package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.table.pot.PotNotifier;
import it.unibo.burraco.view.table.pot.PotView;

/**
 * Controller responsible for managing the "pozzetto" (side pot) acquisition phase.
 * It coordinates the transition when a player exhausts their initial hand,
 * updating both the visual state and notifying the user of the event.
 */
public class PotManager {

    private final Turn model;
    private final PotView view;
    private final PotNotifier notifier;

    /**
     * Constructs a PotManager.
     *
     * @param model    the turn model to retrieve the current player and game state
     * @param view     the view component responsible for the pot's visual representation
     * @param notifier the component used to display or log pot-related notifications
     */
    public PotManager(final Turn model, final PotView view, final PotNotifier notifier) {
        this.model = model;
        this.view = view;
        this.notifier = notifier;
    }

    /**
     * Executes the pot acquisition logic.
     * Checks if the current player is eligible to take the pot and updates 
     * the UI components accordingly.
     *
     * @param isDiscard true if the pot is taken after a discard (turn ends), 
     *                  false if taken "on the fly" (play continues)
     * @return true if the pot was successfully processed, false if the player 
     *         was not in a state to take it
     */
    public boolean handlePot(final boolean isDiscard) {
        final Player p = this.model.getCurrentPlayer();
        if (p.isInPot()) {
            this.notifier.notifyPotTaken(p.getName(), isDiscard);
            this.view.markPotTaken(this.model.isPlayer1Turn());
            
            if (!isDiscard) {
                this.view.refreshHandPanel(this.model.isPlayer1Turn(), p.getHand());
            }
            return true;
        }
        return false;
    }
}
