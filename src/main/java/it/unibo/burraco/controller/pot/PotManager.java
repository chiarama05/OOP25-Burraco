package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.pot.PotView;

public class PotManager {

    private final Turn model;
    private final PotView view;

    public PotManager(final Turn model, final PotView view) {
        this.model = model;
        this.view = view;
    }

    public boolean handlePot(final boolean isDiscard) {
        final Player p = model.getCurrentPlayer();

        if (p.getHand().isEmpty() && !p.isInPot()) {
            p.setInPot(true);
            p.drawPot();

            view.showPotMessage(p.getName(), isDiscard);
            view.markPotTaken(model.isPlayer1Turn());

            if (!isDiscard) {
                view.refreshHandPanel(model.isPlayer1Turn(), p.getHand());
            }
            return true;
        }
        return false;
    }
}
