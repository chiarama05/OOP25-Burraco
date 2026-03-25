package it.unibo.burraco.view.notification.takediscard;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import it.unibo.burraco.controller.drawcard.DrawResult;

public class TakeDiscardNotifierImpl implements TakeDiscardNotifier{

    private final JFrame parent;

    public TakeDiscardNotifierImpl(JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyTakeDiscardError(DrawResult result) {
        String message = switch (result.getStatus()) {
            case ALREADY_DRAWN -> "You have already drawn a card this turn!";
            case EMPTY_DECK -> "The discard pile is empty!"; // O un messaggio specifico per la pila
            default -> "You cannot take the discard pile right now.";
        };

        JOptionPane.showMessageDialog(parent, message, "Take Discard Error", JOptionPane.WARNING_MESSAGE);
    }

}
