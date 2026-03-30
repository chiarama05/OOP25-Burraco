package it.unibo.burraco.view.combination;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link PutCombinationView} that manages the interaction between 
 * the "Put Combination" button and the main table display.
 * This class acts as a bridge between the user interface components and the game logic.
 */
public final class PutCombinationButton implements PutCombinationView {

    private final TableView tableView;
    private boolean isPlayer1;

    /**
     * Constructs a PutCombinationButton associated with a specific TableView.
     * 
     * @param tableView the main graphical view of the game table
     */
    public PutCombinationButton(final TableView tableView) {
        this.tableView = tableView;
    }

    /**
     * Sets the player context for this view component.
     * 
     * @param isPlayer1 true if this button belongs to or acts for Player 1
     */
    public void setIsPlayer1(final boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Retrieves the list of cards currently selected in the player's hand.
     * 
     * @return a new list containing the selected {@link Card} objects
     */
    public List<Card> getSelectedCards() {
        return new ArrayList<>(
            this.tableView.getHandViewForCurrentPlayer(isPlayer1).getSelectedCards());
    }

    /**
     * Returns the player context.
     * 
     * @return true if it is Player 1
     */
    public boolean isPlayer1() {
        return this.isPlayer1;
    }

    /**
     * Registers a listener action to be performed when the physical button is clicked.
     * 
     * @param action a {@link Runnable} containing the logic to execute on click
     */
    public void setOnPutCombination(final Runnable action) {
        this.tableView.getPutComboBtn().addActionListener(finalEvent -> action.run());
    }

    @Override
    public void onCombinationSuccess(final List<Card> combo, final boolean isP1, final Player current) {
        this.updateView(combo, isP1, current);
    }

    @Override
    public void onCombinationTakePot(final List<Card> combo, final boolean isP1, final Player current) {
        this.updateView(combo, isP1, current);
    }

    @Override
    public void onCombinationClose(final List<Card> combo, final boolean isP1, final Player current) {
        this.updateView(combo, isP1, current);
    }

    /**
     * Updates the table and hand views after a combination is placed.
     * This method prevents code duplication.
     * 
     * @param combo   the cards in the combination
     * @param isP1    true if player 1
     * @param current the player instance
     */
    private void updateView(final List<Card> combo, final boolean isP1, final Player current) {
        final List<Card> comboCopy = new ArrayList<>(combo);
        this.tableView.addCombinationToPlayerPanel(comboCopy, isP1);
        this.tableView.getHandViewForCurrentPlayer(isP1).clearSelection();
        this.tableView.refreshHandPanel(isP1, current.getHand());
    }
}
