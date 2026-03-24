package it.unibo.burraco.view.button;

import java.awt.event.ActionListener;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.table.TableViewImpl;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class TakeDiscardButton implements ActionListener{

    private final DrawManager drawManager;
    private final TableViewImpl tableView;
    private final Turn turnManager;
    private final DiscardPile discardPileModel;
    private final it.unibo.burraco.view.discard.DiscardViewImpl discardView;

    public TakeDiscardButton(JButton button, DrawManager drawManager, TableViewImpl tableView, 
                                 Turn turnManager, DiscardPile discardPileModel, 
                                 it.unibo.burraco.view.discard.DiscardViewImpl discardView) {
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
