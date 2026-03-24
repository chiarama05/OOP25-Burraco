package it.unibo.burraco.controller.pot;

import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.table.TableView;

import javax.swing.JOptionPane;

import it.unibo.burraco.model.player.Player;

public class PotManager {

    private final Turn model;
    private final TableView view;

    public PotManager(Turn model, TableView view) {
        this.model = model;
        this.view = view;
    }

    public boolean handlePot(boolean isDiscard) {
       Player p = model.getCurrentPlayer();

    if (p.getHand().isEmpty() && !p.isInPot()) {
        p.setInPot(true);
        p.drawPot(); 
        
        
        String msg = isDiscard ? 
            p.getName() + " You took the pot! You'll see the new cards next turn." : 
            p.getName() + " You took the pot on fly! Keep playing.";
        
        JOptionPane.showMessageDialog(null, msg, "Pot", JOptionPane.INFORMATION_MESSAGE);
        
        view.markPotTaken(model.isPlayer1Turn()); 

        
        if (!isDiscard) {
            view.refreshHandPanel(p); 
        }

        return true;
    }
    return false;
    }
}
