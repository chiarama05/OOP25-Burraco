package it.unibo.burraco.view.discardcard.takediscard;

import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifier;

import javax.swing.*;
import java.util.List;

/**
 * Concrete implementation that links the physical JButton to the "Take Discard" action logic.
 * It coordinates the refresh of the table panels upon success.
 */
public class TakeDiscardButton implements TakeDiscardActionView {

    private final TakeDiscardView view;
    private final TakeDiscardNotifier notifier;

    /** Callback to be executed when the button is clicked. */
    private Runnable onTakeDiscardAction;

    /**
     * @param button the JButton component to attach the listener to.
     * @param view the view interface responsible for panel refreshing.
     * @param notifier the utility to notify errors to the user.
     */
    public TakeDiscardButton(JButton button, TakeDiscardView view, TakeDiscardNotifier notifier) {
        this.view = view;
        this.notifier = notifier;

        // Link the Swing ActionEvent to the internal Runnable callback
        button.addActionListener(e -> {
            if (onTakeDiscardAction != null) {
                onTakeDiscardAction.run();
            }
        });
    }

    /**
     * Registers the logic to be executed when the user triggers the action.
     * @param handler a Runnable containing the controller's logic.
     */
    public void setOnTakeDiscardAction(Runnable handler) {
        this.onTakeDiscardAction = handler;
    }

    @Override
    public void onTakeDiscardSuccess(Player current, List<Card> updatedPile, boolean isPlayer1) {
        // Refresh the UI components to show the cards moved to the hand
        view.refreshHandPanel(isPlayer1, current.getHand());
        view.updateDiscardPile(updatedPile);
    }

    @Override
    public void onTakeDiscardError(DrawResult result) {
        // Delegate the visual error feedback to the notifier
        notifier.notifyTakeDiscardError(result);
    }
}