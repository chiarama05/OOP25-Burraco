package view.button;

import view.table.TableViewImpl;
import model.player.Player;
import model.card.Card;
import core.combination.CombinationValidator;
import core.combination.SetUtils;
import core.combination.StraightUtils;
import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import core.turnvalidation.TurnPlayOutcome;
import core.turnvalidation.TurnValidator;
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
    private final TurnValidator turnValidator;
    
    public PutCombinationController(TableViewImpl tableView, TurnManager turnManager, SelectionCardManager selectionManager, DrawManager drawManager, TurnValidator turnValidator) {
        this.tableView = tableView;
        this.selectionManager = selectionManager;
        this.turnManager=turnManager;
        this.drawManager = drawManager;
        this.turnValidator=turnValidator;
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

        TurnPlayOutcome outcome= turnValidator.canPlayCardsNow(currentPlayer, selected.size());
        if(!outcome.isAllowed()){
            JOptionPane.showMessageDialog(null, outcome.getMessage()!=null ? outcome.getMessage() : "Action not allowed");
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
        
        if(outcome.triggerPotFly()){
            currentPlayer.setInPot(true);
            currentPlayer.drawPot();
            tableView.showPotFly();
            tableView.markPotTaken(tableView.isPlayer1(currentPlayer));
        }

        tableView.refreshHandPanel(currentPlayer);
        selectionManager.clearSelection();
    }
}