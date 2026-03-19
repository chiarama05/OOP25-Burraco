package core.pot;

import model.turn.Turn;
import view.table.TableView;

import javax.swing.JOptionPane;

import model.player.Player;

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
        
        
        String msg = isDiscard ? p.getName() + " You took the pot! You'll see the new cards next turn." : p.getName() + " You took the pot on fly! Keep playing.";
        
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
