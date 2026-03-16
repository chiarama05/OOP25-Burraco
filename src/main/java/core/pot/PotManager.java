package core.pot;

import model.turn.Turn;
import view.notification.GameNotifier;
import view.table.TableView;

import javax.swing.JOptionPane;

import model.player.Player;

public class PotManager {

    private final Turn model;
    private final TableView view;
    private final GameNotifier notifier;

    public PotManager(Turn model, TableView view, GameNotifier notifier) {
        this.model = model;
        this.view = view;
        this.notifier = notifier;
    }

    public boolean handlePot(boolean isDiscard) {
       Player p = model.getCurrentPlayer();

    if (p.getHand().isEmpty() && !p.isInPot()) {
        p.setInPot(true);
        p.drawPot(); 
        
        
        String msg = isDiscard ? 
            p.getName() + " ha preso il pozzetto (con lo scarto)! Vedrai le nuove carte al prossimo turno." : 
            p.getName() + " ha preso il pozzetto al volo!";
        
        JOptionPane.showMessageDialog(null, msg, "Pozzetto", JOptionPane.INFORMATION_MESSAGE);
        
        view.markPotTaken(model.isPlayer1Turn()); 

        
        if (!isDiscard) {
            view.refreshHandPanel(p); 
        }

        return true;
    }
    return false;
    }
}
