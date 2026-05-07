package it.unibo.burraco.view.notification.closure;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class ClosureNotifierImpl implements ClosureNotifier {

    private final JFrame parent;

    /**
     * Constructs a ClosureNotifierImpl.
     *
     * @param parent the parent frame for dialogs
     */
    public ClosureNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyInvalidClosure() {
        JOptionPane.showMessageDialog(parent,
            "You can't close without a Burraco!\nKeep playing to form one!",
            "Cannot close", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void notifyMustTakePotBeforeDiscard() {
        JOptionPane.showMessageDialog(parent,
            "You have no more cards but you haven't taken your pot yet!\n"
            + "The pot has been given to you automatically – keep playing.",
            "Pot Taken Automatically", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyMustFormBurracoBeforeClose() {
        JOptionPane.showMessageDialog(parent,
            "Your hand is empty but you don't have a Burraco yet!\n"
            + "You must reach 7 cards in one of your combinations\n"
            + "(by attaching) before you can close the round.",
            "Burraco Required", JOptionPane.WARNING_MESSAGE);
    }
}
