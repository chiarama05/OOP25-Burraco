package it.unibo.burraco.view.notification.selection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Swing-based implementation of {@link SelectionNotifier}.
 * Uses {@link JOptionPane} to show alerts regarding selection mistakes.
 */
public final class SelectionNotifierImpl implements SelectionNotifier{

    private final JFrame parent;

    /**
     * Constructs a SelectionNotifierImpl.
     * 
     * @param parent the parent {@link JFrame} used to position the dialogs
     */
    public SelectionNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifySelectionError(final String errorCode) {
        final String message = switch (errorCode) {
            case "EMPTY_SELECTION" -> "Select card from your hand first!";
            case "INVALID_COMBINATION" -> "Combination not valid!";
            default -> "Error.";
        };

        JOptionPane.showMessageDialog(parent, message, "Selection error", JOptionPane.WARNING_MESSAGE);
    }

}
