package it.unibo.burraco.view.combination;

import it.unibo.burraco.controller.combination.PutCombinationActionController;
import it.unibo.burraco.controller.combination.PutCombinationController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifier;
import it.unibo.burraco.view.table.TableView;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class PutCombinationButton implements PutCombinationView {

    private final TableView tableView;
    private final GameController gameController;
    private final PutCombinationActionController actionController;

    public PutCombinationButton(TableView tableView,
                                 GameController gameController,
                                 PutCombinationController putComboController,
                                 PutCombinationNotifier notifier) {
        this.tableView = tableView;
        this.gameController = gameController;
        this.actionController = new PutCombinationActionController(gameController, putComboController, notifier);
    }

    public void handlePutCombination() {
        Player current = gameController.getCurrentPlayer();
        List<Card> selected = new ArrayList<>(
                tableView.getHandViewForPlayer(current).getSelectedCards());
        actionController.handle(selected, this);
    }


    @Override
    public void showCombinationError(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void onCombinationSuccess(List<Card> combo, boolean isP1, Player current) {
        tableView.addCombinationToPlayerPanel(combo, isP1);
        tableView.getHandViewForPlayer(current).clearSelection();
        tableView.refreshHandPanel(current);
    }

    @Override
    public void onCombinationTakePot(List<Card> combo, boolean isP1, Player current) {
        tableView.addCombinationToPlayerPanel(combo, isP1);
        tableView.getHandViewForPlayer(current).clearSelection();
        tableView.refreshHandPanel(current);
    }

    @Override
    public void onCombinationClose(List<Card> combo, boolean isP1, Player current) {
        tableView.addCombinationToPlayerPanel(combo, isP1);
        tableView.getHandViewForPlayer(current).clearSelection();
        tableView.refreshHandPanel(current);
    }
}