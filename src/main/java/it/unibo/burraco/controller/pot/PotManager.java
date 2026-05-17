package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.table.BurracoView;

/**
 * Manages pot acquisition.
 * Uses BurracoView directly — no PotView, no PotNotifier.
 */
public class PotManager {

    private final Turn model;
    private final BurracoView view;

    public PotManager(final Turn model, final BurracoView view) {
        this.model = model;
        this.view = view;
    }

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
