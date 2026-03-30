package it.unibo.burraco.view.notification.putcombination;
 
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import it.unibo.burraco.controller.combination.putcombination.PutCombinationResult;

public class PutCombinationNotifierImpl implements PutCombinationNotifier {

    private final JFrame parent;

    public PutCombinationNotifierImpl(JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyCombinationError(PutCombinationResult result) {

        PutCombinationResult.Status status = result.getStatus();
        
        String message = switch (status) {
            case NOT_DRAWN -> "Draw a card first!";
            case NO_CARDS_SELECTED -> "Select cards from your hand first!";
            case INVALID_COMBINATION -> "Invalid combination or not enough cards!";
            case WOULD_GET_STUCK -> """
                You cannot play this combination!

                After placing it you would have only 1 card left,
                but you don't have a Burraco yet and you cannot close.

                You need at least one Burraco before you can reduce
                your hand to 1 card.
                """;
            default -> throw new IllegalArgumentException("Not an error status: " + status);
        };

        String title = switch (status) {
            case WOULD_GET_STUCK -> "Move Not Allowed";
            default -> "Error";
        };

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
