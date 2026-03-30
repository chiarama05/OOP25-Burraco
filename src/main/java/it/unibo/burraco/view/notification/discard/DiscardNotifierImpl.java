package it.unibo.burraco.view.notification.discard;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;

public class DiscardNotifierImpl implements DiscardNotifier{

    private final JFrame parent;

    public DiscardNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyDiscardError(final DiscardResult result) {
        String errorCode = result.getMessage(); // Prende la stringa "NO_BURRACO_ERROR"
    
    String userMessage = switch (errorCode) {
        case "NOT_SELECTED" -> 
            "You must select a card to discard.";
        case "NOT_IN_HAND" -> 
            "The selected card is not in your hand.";
        case "NO_BURRACO_ERROR" -> 
            "You need at least one Burraco to close the round!";
        default -> 
            "An error occurred during discard.";
    };

    JOptionPane.showMessageDialog(parent, userMessage, "Discard Error", JOptionPane.ERROR_MESSAGE);
    }

}
