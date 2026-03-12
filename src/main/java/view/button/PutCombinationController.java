package view.button;

import view.table.TableViewImpl;
import model.player.Player;
import model.card.Card;
import core.combination.CombinationValidator;
import core.combination.SetUtils;
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
            JOptionPane.showMessageDialog(null, "Draw a card first!");
            return;
        }

        Player currentPlayer = turnManager.getCurrentPlayer();
        
        List<Card> selected = selectionManager.getSelectedCards().stream().collect(Collectors.toList());

        if (selected.size() < 3) {
            JOptionPane.showMessageDialog(null, "At least 3 cards are needed to form a scale or a tris!");
            return;
        }

        if (!CombinationValidator.isValidCombination(selected)) {
            JOptionPane.showMessageDialog(null, "Invalid combination!");
            return;
        }

        int handSizeBefore=currentPlayer.getHand().size();
        int reimainingAfter=handSizeBefore-selected.size();

        if(reimainingAfter<1){
            JOptionPane.showMessageDialog(null, "You must discard, you must have at least one card in your hand to discard");
            return;
        }


        if (StraightUtils.isSameSeed(selected) && !SetUtils.isValidSet(selected)) {
            selected = StraightUtils.orderStraight(selected);
        }

        currentPlayer.addCombination(selected);
        currentPlayer.removeCards(selected); 
        
        tableView.addCombinationToPlayerPanel(selected, tableView.isPlayer1(currentPlayer));
        
        if (selected.size() >= 7){
            tableView.getSoundController().playBurracoSound();
        }
        
        tableView.refreshHandPanel(currentPlayer);
        selectionManager.clearSelection();
    }
}