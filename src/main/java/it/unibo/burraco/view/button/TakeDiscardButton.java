package it.unibo.burraco.view.button;

import it.unibo.burraco.controller.buttonLogic.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.discard.DiscardViewImpl;
import it.unibo.burraco.view.table.TableViewImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TakeDiscardButton implements ActionListener {

    private final TakeDiscardController takeDiscardController;
    private final TableViewImpl tableView;
    private final Turn turnModel;
    private final DiscardPile discardPile;
    private final DiscardViewImpl discardView;

    public TakeDiscardButton(JButton button,
                             TakeDiscardController takeDiscardController,
                             TableViewImpl tableView,
                             Turn turnModel,
                             DiscardPile discardPile,
                             DiscardViewImpl discardView) {
        this.takeDiscardController = takeDiscardController;
        this.tableView = tableView;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
        this.discardView = discardView;
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DrawResult result = takeDiscardController.tryTakeDiscard();

        switch (result.getStatus()) {
            case SUCCESS_MULTIPLE:
                Player current = turnModel.getCurrentPlayer();
                tableView.refreshHandPanel(current);
                discardView.updateDiscardPile(discardPile.getCards());
                break;
            case ALREADY_DRAWN:
                JOptionPane.showMessageDialog(null, "You have already drawn in this turn!");
                break;
            case EMPTY_DISCARD:
                JOptionPane.showMessageDialog(null, "The discard pile is empty!");
                break;
            default:
                break;
        }
    }
}