package view.button;

import view.controller.GameController;
import view.table.TableView; 
import model.player.Player;
import model.card.Card;
import core.combination.CombinationValidator;
import core.combination.SetUtils;
import core.combination.StraightUtils;
import core.drawcard.DrawManager;
import core.pot.PotManager;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PutCombinationController {

    private final TableView tableView;
    private final GameController gameController; 
    private final DrawManager drawManager; 
    private final PotManager potManager;

    public PutCombinationController(TableView tableView, GameController gameController,
                                    DrawManager drawManager, PotManager potManager) {
        this.tableView = tableView;
        this.gameController = gameController;
        this.drawManager = drawManager; 
        this.potManager = potManager;
    }

    public void handlePutCombination() {
        
        if (!drawManager.hasDrawn()) {
        JOptionPane.showMessageDialog(null, "Draw a card first!");
        return;
    }

    Player current = gameController.getCurrentPlayer();
    Set<Card> selectedSet = tableView.getHandViewForPlayer(current).getSelectedCards();
    List<Card> selected = new ArrayList<>(selectedSet);

    if (selected.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Select cards from your hand first!");
        return;
    }

    if (selected.size() < 3) {
        JOptionPane.showMessageDialog(null, "At least 3 cards are needed!");
        return;
    }

    if (!CombinationValidator.isValidCombination(selected)) {
        JOptionPane.showMessageDialog(null, "Invalid combination!");
        return;
    }

  
    if (StraightUtils.isSameSeed(selected) && !SetUtils.isValidSet(selected)) {
        selected = StraightUtils.orderStraight(selected);
    }


    current.addCombination(selected);
    current.removeCards(selected); 

    tableView.addCombinationToPlayerPanel(selected, gameController.isPlayer1(current));

    if (selected.size() >= 7) {
        gameController.getSoundController().playBurracoSound();
    }

    if (current.hasFinishedCards() && !current.isInPot()) {
        potManager.handlePot(false); 
    } else {
       
        tableView.refreshHandPanel(current);
    }
    tableView.getHandViewForPlayer(current).clearSelection();
    }
}