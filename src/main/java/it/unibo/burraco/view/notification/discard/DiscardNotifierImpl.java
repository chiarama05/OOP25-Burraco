package it.unibo.burraco.view.notification.discard;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Swing-based implementation of {@link DiscardNotifier}.
 * Translates discard error statuses into user-facing dialog messages.
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
    public void notifyDiscardError(final DiscardResult.Status status) {
        final String message = switch (status) {
            case NOT_DRAWN ->
                "You must draw a card before discarding!";
            case SELECT_ONE ->
                "Select only one card to discard!";
            case NOT_SELECTED ->
                "You must select a card to discard.";
            case NOT_IN_HAND ->
                "The selected card is not in your hand.";
            case NO_BURRACO_ERROR ->
                """
                You need at least one Burraco to close the round!
                Keep playing to form one.
                """;
            default ->
                throw new IllegalArgumentException("Unexpected discard error status: " + status);
        };

        final String title = switch (status) {
            case NO_BURRACO_ERROR -> "Cannot Close";
            default -> "Discard Error";
        };

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
