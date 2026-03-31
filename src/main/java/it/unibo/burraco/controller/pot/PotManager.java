package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.pot.PotView;

/**
 * Controller class responsible for managing the transition to the "pot" (pozzetto).
 * It checks if a player is eligible to take the pot and updates both model and view.
 */
public class PotManager {
    private final Turn model;
    private final PotView view;

    /**
     * Constructs a PotManager with the specified model and view.
     *
     * @param model the turn model
     * @param view  the pot view
     */
    public PotManager(final Turn model, final PotView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles the logic for taking the pot.
     * If the current player's hand is empty and they haven't taken the pot yet,
     * it draws the pot and updates the UI.
     *
     * @param isDiscard true if the pot was triggered by a discard, false if "on the fly"
     * @return true if the pot was successfully taken, false otherwise
     */
    public boolean handlePot(final boolean isDiscard) {
        final Player p = this.model.getCurrentPlayer();

        if (p.getHand().isEmpty() && !p.isInPot()) {
            p.setInPot(true);
            p.drawPot();

            this.view.showPotMessage(p.getName(), isDiscard);
            this.view.markPotTaken(this.model.isPlayer1Turn());

            if (!isDiscard) {
                this.view.refreshHandPanel(this.model.isPlayer1Turn(), p.getHand());
            }
            return true;
        }
        return false;
    }
}
