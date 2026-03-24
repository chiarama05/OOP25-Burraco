package it.unibo.burraco.view.button;

import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.controller.controller.GameController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class PutCombinationButton {
    private final TableView tableView;
    private final GameController gameController;
    private final DrawManager drawManager;
    private final PotManager potManager;
    private final ClosureManager closureManager;

    public PutCombinationButton(TableView tableView, GameController gameController,DrawManager drawManager, PotManager potManager, ClosureManager closureManager) {
        
        this.tableView = tableView;
        this.gameController = gameController;
        this.drawManager = drawManager;
        this.potManager = potManager;
        this.closureManager=closureManager;
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


        //simulate the outcome before touching the model, it blocks the action if putting down 
        //this cards would leave the player with 1 card and no burraco
        if (ClosureValidator.wouldGetStuckAfterPutCombo(current, selected, selected.size())) {
            JOptionPane.showMessageDialog(null,"You cannot play this combination!\n\n"+ "After placing it you would have only 1 card left,\n"+ "but you don't have a Burraco yet and you cannot close.\n\n"+ "You need at least one Burraco before you can reduce\n"+ "your hand to 1 card.","Move Not Allowed", JOptionPane.WARNING_MESSAGE);
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


        tableView.getHandViewForPlayer(current).clearSelection();

        
        ClosureState state = ClosureValidator.evaluate(current);


        // Hand is empty and pot not yet taken → take pot on-the-fly.
        if (state == ClosureState.ZERO_CARDS_NO_POT) {
            potManager.handlePot(false);
            return;
        }


        // Hand is empty, pot already taken, burraco present → round ends.
        if (state == ClosureState.CAN_CLOSE) {
            closureManager.handleStateAfterAction(current);
            return;
        }


        // Hand is empty, pot already taken, but NO burraco yet.
        if (state == ClosureState.ZERO_CARDS_NO_BURRACO) {
            closureManager.handleStateAfterAction(current);
            tableView.refreshHandPanel(current);
            return;
        }

        // Normal case: player still has cards → just refresh.
        tableView.refreshHandPanel(current);




        // ## 2 version
        // If the hand is empty and pot not taken → PotManager handles the draw (pot fly)
        // ClosureManager then decides whether the player can continue or the round ends.
        /*if(current.getHand().isEmpty() && !current.isInPot()){
            potManager.handlePot(false);
            closureManager.handleStateAfterAction(current);
        }
        else{
            boolean blocking=closureManager.handleStateAfterAction(current);
            if(!blocking){
                tableView.refreshHandPanel(current);
            }
        }
        tableView.getHandViewForPlayer(current).clearSelection();
        tableView.refreshHandPanel(current);    
        */


        // ## default version
        /*if (current.hasFinishedCards() && !current.isInPot()) {
            potManager.handlePot(false);
        } else {
            tableView.refreshHandPanel(current);
        }
        tableView.getHandViewForPlayer(current).clearSelection();
        tableView.refreshHandPanel(current);    
        */
    }
}