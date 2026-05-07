package it.unibo.burraco.view.notification.deck;

import it.unibo.burraco.controller.drawcard.DrawResult;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Swing-based implementation of {@link DeckNotifier}.
 * Uses {@link JOptionPane} to display warning messages to the player.
 */
public final class DeckNotifierImpl implements DeckNotifier {

    private final JFrame parent;

    /**
     * Constructs a DeckNotifierImpl.
     *
     * @param parent the parent frame used to center the warning dialogs
     */
    public DeckNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyDrawError(final DrawResult result) {
        final String message = switch (result.getStatus()) {
            case ALREADY_DRAWN ->
                "You have already drawn a card this turn!";
            case EMPTY_DECK ->
                "The deck is empty! You must draw from the discard pile.";
            default ->
                throw new IllegalArgumentException(
                    "Unexpected draw error status: " + result.getStatus());
        };

        JOptionPane.showMessageDialog(parent, message, "Draw Error", JOptionPane.WARNING_MESSAGE);
    }
}
