package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.table.BurracoView;

/**
 * Manages pot acquisition.
 * Uses BurracoView directly.
 */
public class PotManager {

    private final Turn model;
    private final BurracoView view;

    /**
     * Constructs a new PotManager.
     *
     * @param model the current turn state model
     * @param view  the main view interface used to send notifications
     */
    public PotManager(final Turn model, final BurracoView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles the logic and view transitions when a player collects their pot.
     *
     * @param isDiscard true if the pot was taken on a discard action, false if on the fly
     * @return true if the current player actually collected the pot, false otherwise
     */
    public boolean handlePot(final boolean isDiscard) {
        final Player p = this.model.getCurrentPlayer();
        if (p.isInPot()) {
            this.view.notifyPotTaken(p.getName(), isDiscard);
            this.view.markPotTaken(this.model.isPlayer1Turn());
            if (!isDiscard) {
                this.view.refreshHandPanel(
                    this.model.isPlayer1Turn(), p.getHand());
            }
            return true;
        }
        return false;
    }
}
