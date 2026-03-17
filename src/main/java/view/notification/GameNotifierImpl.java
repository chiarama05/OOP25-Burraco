package view.notification;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameNotifierImpl implements GameNotifier{

    private final JFrame parent;

    public GameNotifierImpl(JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyPotTakenNextTurn() {
        JOptionPane.showMessageDialog(parent, "You have taken your pot! You can play it in the NEXT turn!", "Pot", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyPotTakenFly() {
        JOptionPane.showMessageDialog(parent, "You close your hand on 'fly', you can continue to play in this same turn!", "Pot", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyInvalidClosure() {
        JOptionPane.showMessageDialog(parent, "You can't discard your last card without even done a Burraco!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void notifyVictory(String winnerName) {
        JOptionPane.showMessageDialog(parent, "Congratulation " + winnerName + "! Hai vinto la partita!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyMustDraw() {
        JOptionPane.showMessageDialog(parent, "Devi pescare dal mazzo o raccogliere gli scarti prima di scartare!", "Azione richiesta", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void notifySelectionError(String message) {
        JOptionPane.showMessageDialog(parent, message, "Error Selection", JOptionPane.WARNING_MESSAGE);
    }

}
