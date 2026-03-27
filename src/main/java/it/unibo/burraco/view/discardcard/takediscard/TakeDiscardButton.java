package it.unibo.burraco.view.discardcard.takediscard;

import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifier;

import javax.swing.*;
import java.util.List;

public class TakeDiscardButton implements TakeDiscardActionView {

    private final TakeDiscardView view;
    private final TakeDiscardNotifier notifier;
    
    private Runnable onTakeDiscardAction;

    public TakeDiscardButton(JButton button, TakeDiscardView view, TakeDiscardNotifier notifier) {
        this.view = view;
        this.notifier = notifier;
        button.addActionListener(e -> {
            if (onTakeDiscardAction != null) onTakeDiscardAction.run();
        });
    }

    public void setOnTakeDiscardAction(Runnable handler) {
        this.onTakeDiscardAction = handler;
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