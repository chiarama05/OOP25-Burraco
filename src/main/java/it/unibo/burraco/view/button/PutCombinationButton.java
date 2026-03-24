package it.unibo.burraco.view.button;

import it.unibo.burraco.controller.buttonLogic.PutCombinationController;
import it.unibo.burraco.controller.buttonLogic.PutCombinationResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class PutCombinationButton {

    private final TableView tableView;
    private final GameController gameController;
    private final PutCombinationController putComboController;

    public PutCombinationButton(TableView tableView,
                                 GameController gameController,
                                 PutCombinationController putComboController) {
        this.tableView = tableView;
        this.gameController = gameController;
        this.putComboController = putComboController;
    }

    public void handlePutCombination() {

        Player current = gameController.getCurrentPlayer();
        List<Card> selected = new ArrayList<>(
                tableView.getHandViewForPlayer(current).getSelectedCards());

   
        PutCombinationResult result = putComboController.tryPutCombination(selected);

      
        switch (result.getStatus()) {

            case NOT_DRAWN:
                JOptionPane.showMessageDialog(null, "Draw a card first!");
                break;

            case NO_CARDS_SELECTED:
                JOptionPane.showMessageDialog(null, "Select cards from your hand first!");
                break;

            case WOULD_GET_STUCK:
                JOptionPane.showMessageDialog(null,
                        "You cannot play this combination!\n\n"
                        + "After placing it you would have only 1 card left,\n"
                        + "but you don't have a Burraco yet and you cannot close.\n\n"
                        + "You need at least one Burraco before you can reduce\n"
                        + "your hand to 1 card.",
                        "Move Not Allowed", JOptionPane.WARNING_MESSAGE);
                break;

            case INVALID_COMBINATION:
                JOptionPane.showMessageDialog(null, "Invalid combination or not enough cards!");
                break;

            case SUCCESS:
            case SUCCESS_BURRACO:
                tableView.addCombinationToPlayerPanel(result.getProcessedCombo(), result.isPlayer1());
                tableView.getHandViewForPlayer(current).clearSelection();
                tableView.refreshHandPanel(current);
                break;

            case SUCCESS_TAKE_POT:
                tableView.addCombinationToPlayerPanel(result.getProcessedCombo(), result.isPlayer1());
                tableView.getHandViewForPlayer(current).clearSelection();
                break;

            case SUCCESS_CLOSE:
                tableView.addCombinationToPlayerPanel(result.getProcessedCombo(), result.isPlayer1());
                tableView.getHandViewForPlayer(current).clearSelection();
                break;

            case SUCCESS_STUCK:
                tableView.addCombinationToPlayerPanel(result.getProcessedCombo(), result.isPlayer1());
                tableView.getHandViewForPlayer(current).clearSelection();
                tableView.refreshHandPanel(current);
                break;
        }
    }
}