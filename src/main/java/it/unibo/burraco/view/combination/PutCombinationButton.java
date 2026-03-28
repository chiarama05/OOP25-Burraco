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
public class PutCombinationButton implements PutCombinationView {

    private final TableView tableView;
    private boolean isPlayer1;

    /**
     * Constructs a PutCombinationButton associated with a specific TableView.
     * @param tableView the main graphical view of the game table
     */
    public PutCombinationButton(TableView tableView) {
        this.tableView = tableView;
    }

    /**
     * Sets the player context for this view component.
     * @param isPlayer1 true if this button belongs to or acts for Player 1
     */
    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Retrieves the list of cards currently selected in the player's hand.
     * @return a new list containing the selected {@link Card} objects
     */
    public List<Card> getSelectedCards() {
        return new ArrayList<>(
            tableView.getHandViewForCurrentPlayer(isPlayer1).getSelectedCards());
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    /**
     * Registers a listener action to be performed when the physical button is clicked.
     * @param action a {@link Runnable} containing the logic to execute on click
     */
    public void setOnPutCombination(Runnable action) {
        tableView.getPutComboBtn().addActionListener(e -> action.run());
    }

    @Override
    public void onCombinationSuccess(List<Card> combo, boolean isP1, Player current) {
        tableView.addCombinationToPlayerPanel(combo, isP1);
        tableView.getHandViewForCurrentPlayer(isP1).clearSelection();
        tableView.refreshHandPanel(isP1, current.getHand());
    }

    @Override
    public void onCombinationTakePot(List<Card> combo, boolean isP1, Player current) {
        tableView.addCombinationToPlayerPanel(combo, isP1);
        tableView.getHandViewForCurrentPlayer(isP1).clearSelection();
        tableView.refreshHandPanel(isP1, current.getHand());
    }

    @Override
    public void onCombinationClose(List<Card> combo, boolean isP1, Player current) {
        tableView.addCombinationToPlayerPanel(combo, isP1);
        tableView.getHandViewForCurrentPlayer(isP1).clearSelection();
        tableView.refreshHandPanel(isP1, current.getHand());
    }
}