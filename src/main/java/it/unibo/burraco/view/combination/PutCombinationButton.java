package it.unibo.burraco.view.combination;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;
import java.util.ArrayList;
import java.util.List;

public class PutCombinationButton implements PutCombinationView {

    private final TableView tableView;
    private boolean isPlayer1;

    public PutCombinationButton(TableView tableView) {
        this.tableView = tableView;
    }

    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    public List<Card> getSelectedCards() {
        return new ArrayList<>(
            tableView.getHandViewForCurrentPlayer(isPlayer1).getSelectedCards());
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

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