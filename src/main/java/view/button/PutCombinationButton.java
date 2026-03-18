package view.button;

import view.table.TableView;
import model.player.Player;
import model.card.Card;
import core.controller.GameController;
import core.drawcard.DrawManager;
import core.pot.PotManager;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PutCombinationButton {
    private final TableView tableView;
    private final GameController gameController;
    private final DrawManager drawManager;
    private final PotManager potManager;

    public PutCombinationButton(TableView tableView, GameController gameController,
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
        List<Card> selected = new ArrayList<>(tableView.getHandViewForPlayer(current).getSelectedCards());

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select cards from your hand first!");
            return;
        }

   
        List<Card> processedCombo = gameController.getCombinationController().tryPutCombination(current, selected);

        if (processedCombo == null) {
            JOptionPane.showMessageDialog(null, "Invalid combination or not enough cards!");
            return;
        }

       
        tableView.addCombinationToPlayerPanel(processedCombo, gameController.isPlayer1(current));

        if (processedCombo.size() >= 7) {
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