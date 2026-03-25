package it.unibo.burraco.view.discard;

import it.unibo.burraco.controller.discardcard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TakeDiscardButton implements ActionListener {

    private final TakeDiscardController takeDiscardController;
    private final TakeDiscardView view;   
    private final Turn turnModel;
    private final DiscardPile discardPile;

    public TakeDiscardButton(JButton button,
                             TakeDiscardController takeDiscardController,
                             TakeDiscardView view,
                             Turn turnModel,
                             DiscardPile discardPile) {
        this.takeDiscardController = takeDiscardController;
        this.view = view;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DrawResult result = takeDiscardController.tryTakeDiscard();

        switch (result.getStatus()) {
            case SUCCESS_MULTIPLE -> {
                Player current = turnModel.getCurrentPlayer();
                view.refreshHandPanel(current);
                view.updateDiscardPile(discardPile.getCards());
            }
            case ALREADY_DRAWN ->
                JOptionPane.showMessageDialog(null, "You have already drawn in this turn!");
            case EMPTY_DISCARD ->
                JOptionPane.showMessageDialog(null, "The discard pile is empty!");
            default -> {}
        }
    }
}