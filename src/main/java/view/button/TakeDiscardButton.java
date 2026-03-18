package view.button;

import java.awt.event.ActionListener;
import core.drawcard.DrawManager;
import core.drawcard.DrawResult;
import model.discard.DiscardPile;
import model.player.Player;
import model.turn.Turn;
import view.table.TableViewImpl;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class TakeDiscardButton implements ActionListener{

    private final DrawManager drawManager;
    private final TableViewImpl tableView;
    private final Turn turnManager;
    private final DiscardPile discardPileModel;
    private final view.discard.DiscardViewImpl discardView;

    public TakeDiscardButton(JButton button, DrawManager drawManager, TableViewImpl tableView, 
                                 Turn turnManager, DiscardPile discardPileModel, 
                                 view.discard.DiscardViewImpl discardView) {
        this.drawManager = drawManager;
        this.tableView = tableView;
        this.turnManager = turnManager;
        this.discardPileModel = discardPileModel;
        this.discardView = discardView;
        
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player current = turnManager.getCurrentPlayer();
        
        DrawResult result = drawManager.drawFromDiscard(current, discardPileModel.getCards());

        if (result.getStatus() == DrawResult.Status.SUCCESS_MULTIPLE) {
           
            tableView.refreshHandPanel(current);
            
           
            discardView.updateDiscardPile(discardPileModel.getCards());
            
        } else if (result.getStatus() == DrawResult.Status.ALREADY_DRAWN) {
            JOptionPane.showMessageDialog(null, "You have already drawn in this turn!");
        } else if (result.getStatus() == DrawResult.Status.EMPTY_DISCARD) {
            JOptionPane.showMessageDialog(null, "The discard pile is empty!");
        }
    }
}
