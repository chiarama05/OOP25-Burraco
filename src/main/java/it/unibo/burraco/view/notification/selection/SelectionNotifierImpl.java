package it.unibo.burraco.view.notification.selection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class SelectionNotifierImpl implements SelectionNotifier{

    private final JFrame parent;

    public SelectionNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifySelectionError(final String errorCode) {
        String message = switch (errorCode) {
            case "EMPTY_SELECTION" -> "Select card from your hand first!";
            case "INVALID_COMBINATION" -> "Combination not valid!";
            default -> "Error.";
        };

        JOptionPane.showMessageDialog(parent, message, "Selection error", JOptionPane.WARNING_MESSAGE);
    }

}
