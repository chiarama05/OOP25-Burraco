package it.unibo.burraco.view.pot;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Swing-based implementation of {@link PotNotifier}.
 */
public final class PotNotifierImpl implements PotNotifier {

    private final JFrame parent;

    /**
     * Constructs a PotNotifierImpl.
     *
     * @param parent the parent frame used to center the dialogs
     */
    public PotNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyPotTaken(final String playerName, final boolean isDiscard) {
        final String message = isDiscard
            ? playerName + " You took the pot! You'll see the new cards next turn."
            : playerName + " You took the pot on fly! Keep playing.";
        JOptionPane.showMessageDialog(parent, message, "Pot", JOptionPane.INFORMATION_MESSAGE);
    }
}