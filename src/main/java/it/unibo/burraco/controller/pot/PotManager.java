package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.pot.PotNotifier;
import it.unibo.burraco.view.pot.PotView;

/**
 * Controller class responsible for managing the transition to the pot (pozzetto).
 */
public class PotManager {

    private final Turn model;
    private final PotView view;
    private final PotNotifier notifier;

    /**
     * Constructs a PotManager with the specified model, view and notifier.
     *
     * @param model    the turn model
     * @param view     the pot view for visual updates
     * @param notifier the notifier for pot-related messages
     */
    public PotManager(final Turn model, final PotView view, final PotNotifier notifier) {
        this.model = model;
        this.view = view;
        this.notifier = notifier;
    }

    /**
     * Handles the logic for taking the pot.
     *
     * @param isDiscard true if the pot was triggered by a discard, false if on the fly
     * @return true if the pot was successfully taken, false otherwise
     */
    public boolean handlePot(final boolean isDiscard) {
        final Player p = this.model.getCurrentPlayer();

        if (p.getHand().isEmpty() && !p.isInPot()) {
            p.setInPot(true);
            p.drawPot();

            this.notifier.notifyPotTaken(p.getName(), isDiscard); // era view.showPotMessage
            this.view.markPotTaken(this.model.isPlayer1Turn());

            if (!isDiscard) {
                this.view.refreshHandPanel(this.model.isPlayer1Turn(), p.getHand());
            }
            return true;
        }
        return false;
    }
}