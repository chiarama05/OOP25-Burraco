package view.button;

import view.table.TableViewImpl;
import model.player.Player;
import model.card.Card;
import core.combination.CombinationValidator;
import core.combination.StraightUtils;
import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import model.turn.TurnManager;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PutCombinationController {

    private final TableViewImpl tableView;
    private final TurnManager turnManager;
    private final SelectionCardManager selectionManager;
    private final DrawManager drawManager;
    
    public PutCombinationController(TableViewImpl tableView, TurnManager turnManager, SelectionCardManager selectionManager, DrawManager drawManager) {
        this.tableView = tableView;
        this.selectionManager = selectionManager;
        this.turnManager=turnManager;
        this.drawManager = drawManager;

    }

    public void handlePutCombination() {
        if (!drawManager.hasDrawn()) {
        JOptionPane.showMessageDialog(null, "You can't put down combinations before you draw!");
        return;
    }
        Player currentPlayer = turnManager.getCurrentPlayer();

        // Recupera le carte selezionate tramite SelectionCardManager
        Set<Card> selectedSet = selectionManager.getSelectedCards();
        List<Card> selected = selectedSet.stream().filter(currentPlayer::hasCard) .collect(Collectors.toList());

        if(selected.size() < 3) {
        JOptionPane.showMessageDialog(null, "At least a 'tris' is needed!");
        return;
        }

        if(!CombinationValidator.isValidCombination(selected)) {
            JOptionPane.showMessageDialog(null, "Invalid combination!");
            return;
        }

        if(StraightUtils.isSameSeed(selected)) {
            selected = StraightUtils.orderStraight(selected);
        }

        
        currentPlayer.removeCards(selected);
        tableView.addCombinationToPlayerPanel(selected, tableView.isPlayer1(currentPlayer));
        tableView.refreshHandPanel(currentPlayer);

        if(currentPlayer.hasFinishedCards() && !currentPlayer.isInPot()){
            currentPlayer.setInPot(true);
            currentPlayer.drawPot();
            tableView.refreshHandPanel(currentPlayer);
            tableView.showPotFly();
        }

        selectionManager.clearSelection();

    }
}