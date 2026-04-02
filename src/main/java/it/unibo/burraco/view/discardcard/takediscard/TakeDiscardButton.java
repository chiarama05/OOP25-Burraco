package it.unibo.burraco.view.discardcard.takediscard;

import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifier;

import javax.swing.JButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation that links the physical JButton to the "Take Discard" action logic.
 * It coordinates the refresh of the table panels upon success.
 */
public final class TakeDiscardButton implements TakeDiscardActionView {

    private final TakeDiscardView view;
    private final TakeDiscardNotifier notifier;
    private Runnable onTakeDiscardAction;

    /**
     * Constructs a TakeDiscardButton and registers the listener on the Swing component.
     *
     * @param button   the JButton component to attach the listener to.
     * @param view     the view interface responsible for panel refreshing.
     * @param notifier the utility to notify errors to the user.
     */
    public TakeDiscardButton(final JButton button, final TakeDiscardView view, final TakeDiscardNotifier notifier) {
        this.view = view;
        this.notifier = notifier;

        // Link the Swing ActionEvent to the internal Runnable callback
        button.addActionListener(e -> {
            if (this.onTakeDiscardAction != null) {
                this.onTakeDiscardAction.run();
            }
        });
    }

    /**
     * Registers the logic to be executed when the user triggers the action.
     *
     * @param handler a Runnable containing the controller's logic.
     */
    public void setOnTakeDiscardAction(final Runnable handler) {
        this.onTakeDiscardAction = handler;
    }

    @Override
    public void onTakeDiscardSuccess(final Player current, final List<Card> updatedPile, final boolean isPlayer1) {
        this.view.updateDiscardPile(new ArrayList<>(updatedPile));
        this.view.refreshHandPanel(isPlayer1, new ArrayList<>(current.getHand()));
    }

    @Override
    public void onTakeDiscardError(final DrawResult result) {
        this.notifier.notifyTakeDiscardError(result);
    }
}
