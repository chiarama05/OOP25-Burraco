package it.unibo.burraco.view.discard;

import it.unibo.burraco.controller.discardcard.TakeDiscardActionController;
import it.unibo.burraco.controller.discardcard.TakeDiscardController;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TakeDiscardButton implements ActionListener, TakeDiscardActionView {

    private final TakeDiscardView view;
    private final TakeDiscardActionController actionController;

    public TakeDiscardButton(JButton button,
                             TakeDiscardController takeDiscardController,
                             TakeDiscardView view,
                             Turn turnModel,
                             DiscardPile discardPile) {
        this.view = view;
        this.actionController = new TakeDiscardActionController(
                takeDiscardController, turnModel, discardPile);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionController.handle(this);
    }


    @Override
    public void onTakeDiscardSuccess(Player current, List<Card> updatedPile) {
        view.refreshHandPanel(current);
        view.updateDiscardPile(updatedPile);
    }

    @Override
    public void onTakeDiscardError(String errorCode) {
        switch (errorCode) {
            case "already_drawn" ->
                JOptionPane.showMessageDialog(null, "You have already drawn in this turn!");
            case "empty_discard" ->
                JOptionPane.showMessageDialog(null, "The discard pile is empty!");
        }
    }
}