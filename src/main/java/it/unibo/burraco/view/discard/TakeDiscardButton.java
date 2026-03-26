package it.unibo.burraco.view.discard;

import it.unibo.burraco.controller.discardcard.TakeDiscardActionController;
import it.unibo.burraco.controller.discardcard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TakeDiscardButton implements ActionListener, TakeDiscardActionView {

    private final TakeDiscardView view;
    private final TakeDiscardActionController actionController;
    private final TakeDiscardNotifier notifier;

    public TakeDiscardButton(JButton button,
                             TakeDiscardController takeDiscardController,
                             TakeDiscardView view,
                             Turn turnModel,
                             DiscardPile discardPile,
                            TakeDiscardNotifier notifier) {
        this.view = view;
        this.notifier = notifier;
        this.actionController = new TakeDiscardActionController(
                takeDiscardController, turnModel, discardPile);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionController.handle(this);
    }


    @Override
    public void onTakeDiscardSuccess(Player current, List<Card> updatedPile, boolean isPlayer1) {
    view.refreshHandPanel(isPlayer1, current.getHand());
    view.updateDiscardPile(updatedPile);
    }

    @Override
    public void onTakeDiscardError(DrawResult result) {
        notifier.notifyTakeDiscardError(result);
    }
}