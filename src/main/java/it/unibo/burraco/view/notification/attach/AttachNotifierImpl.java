package it.unibo.burraco.view.notification.attach;

import it.unibo.burraco.controller.attach.AttachResult;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Swing-based implementation of {@link AttachNotifier}.
 * Displays error messages using {@link JOptionPane} dialogs.
 */
public final class AttachNotifierImpl implements AttachNotifier {

    private final JFrame parent;

    /**
     * Creates a new AttachNotifierImpl.
     *
     * @param parent the parent frame used to center the notification dialogs
     */
    public AttachNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyAttachError(final AttachResult result) {
        final String message = switch (result) {
            case NOT_DRAWN ->
                "You have to draw first!";
            case WRONG_PLAYER ->
                "You can only attach cards to your own combinations!";
            case NO_CARDS_SELECTED ->
                "Select the card from your hand first!";
            case INVALID_COMBINATION ->
                """
                Invalid move: the resulting combination would not be valid!
                (wrong suit, too many wildcards, or broken sequence)
                """;
            case WOULD_GET_STUCK ->
                """
                You cannot attach this card!

                After attaching you would have only 1 card left,
                but you don't have a Burraco yet and you cannot close.

                You need at least one Burraco before you can reduce
                your hand to 1 card.
                """;
            case ATTACH_FAILED ->
                "These cards cannot be attached!";
            default ->
                throw new IllegalArgumentException("Not an error result: " + result);
        };

        final String title = switch (result) {
            case INVALID_COMBINATION, WOULD_GET_STUCK -> "Move Not Allowed";
            default -> "Error";
        };

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
