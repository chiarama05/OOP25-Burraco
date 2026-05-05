package it.unibo.burraco.view.notification.discard;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;

/**
 * Swing-based implementation of {@link DiscardNotifier}.
 * It translates internal error codes into human-readable messages displayed via JOptionPane.
 */
public final class DiscardNotifierImpl implements DiscardNotifier {

    private final JFrame parent;

    /**
     * Constructs a DiscardNotifierImpl.
     *
     * @param parent the parent frame used to center the error dialogs
     */
    public DiscardNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyDiscardError(final DiscardResult result) {
        final String errorCode = result.getMessage();

        final String userMessage = switch (errorCode) {
            case "NOT_SELECTED" ->
                "You must select a card to discard.";
            case "NOT_IN_HAND" ->
                "The selected card is not in your hand.";
            case "NO_BURRACO_ERROR" ->
                "You need at least one Burraco to close the round!";
            default -> null;
        };

    JOptionPane.showMessageDialog(parent, userMessage, "Discard Error", JOptionPane.ERROR_MESSAGE);
    }
}
