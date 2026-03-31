package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.pot.PotView;



/**
 * Handles the logic for automatically drawing the "pot" (pozzetto) when a player
 * empties their hand for the first time.
 * <p>
 * This manager is context-aware: it checks both the hand state and the pot flag
 * before acting, and it conditionally refreshes the hand panel depending on whether
 * the trigger came from a discard action or another game action.
 * </p>
 */
public class PotManager {

    private final Turn model;
    private final PotView view;



    /**
     * Constructs a PotManager.
     *
     * @param model the turn model used to retrieve the current player and the player-turn flag
     * @param view  the pot view responsible for displaying feedback and refreshing UI panels
     */
    public PotManager(final Turn model, final PotView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Checks whether the current player should draw the pot, and if so, executes the draw.
     *
     * @param isDiscard {@code true} if the pot draw was triggered by a discard action,
     *                  {@code false} if triggered by another action (put combination, attach)
     * @return {@code true} if the pot was actually drawn, {@code false} if the conditions
     *         were not met and nothing happened
     */
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
