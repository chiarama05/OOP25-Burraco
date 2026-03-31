package it.unibo.burraco.view.notification.takediscard;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import it.unibo.burraco.controller.drawcard.DrawResult;

/**
 * Swing-based implementation of {@link TakeDiscardNotifier}.
 * Provides visual feedback via {@link JOptionPane} dialogs.
 */
public final class TakeDiscardNotifierImpl implements TakeDiscardNotifier{

    private final JFrame parent;

    /**
     * Constructs a TakeDiscardNotifierImpl.
     *
     * @param parent the parent {@link JFrame} used to center the dialogs
     */
    public TakeDiscardNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyTakeDiscardError(final DrawResult result) {
        final String message = switch (result.getStatus()) {
            case ALREADY_DRAWN -> "You have already drawn a card this turn!";
            case EMPTY_DECK -> "The discard pile is empty!"; // O un messaggio specifico per la pila
            default -> "You cannot take the discard pile right now.";
        };

        JOptionPane.showMessageDialog(parent, message, "Take Discard Error", JOptionPane.WARNING_MESSAGE);
    }

}
