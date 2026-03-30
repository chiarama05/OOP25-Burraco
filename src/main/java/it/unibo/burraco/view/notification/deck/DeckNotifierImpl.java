package it.unibo.burraco.view.notification.deck;

import it.unibo.burraco.controller.drawcard.DrawResult;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DeckNotifierImpl implements DeckNotifier{

    private final JFrame parent;

    public DeckNotifierImpl(final JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void notifyDrawError(final DrawResult result) {
        String message = switch (result.getStatus()) {
            case ALREADY_DRAWN -> 
                "You have already drawn a card this turn!";
            case EMPTY_DECK -> 
                "The deck is empty! You must draw from the discard pile.";
            default -> null;
        };

        if (message != null) {
            JOptionPane.showMessageDialog(parent, message, "Draw Error", JOptionPane.WARNING_MESSAGE);
        }
    }

}
