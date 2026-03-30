package it.unibo.burraco.view.notification.game;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class GameNotifierImpl implements GameNotifier {

    private final JFrame parent;

    public GameNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyPotTakenNextTurn() {
        JOptionPane.showMessageDialog(parent, 
            "You have taken your pot! You can play it in the NEXT turn!", 
            "Pot", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyPotTakenFly() {
        JOptionPane.showMessageDialog(parent, 
            "You close your hand on 'fly', you can continue to play in this same turn!", 
            "Pot", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyInvalidClosure() {
        JOptionPane.showMessageDialog(parent, 
            "You can't close without a Burraco!\nKeep playing to form one!", 
            "Cannot close", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void notifyVictory(final String winnerName) {
        JOptionPane.showMessageDialog(parent, 
            "Congratulation " + winnerName + "! You won the match!", 
            "Victory!", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyMustDraw() {
        JOptionPane.showMessageDialog(parent, 
            "You must draw a card (from the deck or the discard pile) before discarding!", 
            "Action Required", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void notifySelectionError(final String message) {
        JOptionPane.showMessageDialog(parent, message, "Error Selection", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void notifyMustTakePotBeforeDiscard() {
        JOptionPane.showMessageDialog(parent,"You have no more cards but you haven't taken your pot yet!\n"
        + "The pot has been given to you automatically – keep playing.",
        "Pot Taken Automatically", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyMustFormBurracoBeforeClose() {
        JOptionPane.showMessageDialog(parent,"Your hand is empty but you don't have a Burraco yet!\n"
        + "You must reach 7 cards in one of your combinations\n"
        + "(by attaching) before you can close the round.",
        "Burraco Required", JOptionPane.WARNING_MESSAGE);
    }
}
