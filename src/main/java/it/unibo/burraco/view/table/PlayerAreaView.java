package it.unibo.burraco.view.table;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * Visual component representing the lower area of the game table.
 * It organizes the discard pile, the deck, and the current player's hand.
 */
public final class PlayerAreaView extends JPanel {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs the player area view.
     *
     * @param discardComponent the component displaying the discard pile
     * @param deckView         the component displaying the deck
     * @param handContainer    the panel that will contain the player's hand
     * @param backgroundColor  the background color for the panels
     */
    public PlayerAreaView(
            final JComponent discardComponent,
            final JPanel deckView,
            final JPanel deckPanel,
            final Color lightgreen) {
        this.setLayout(new BorderLayout());
        this.setBackground(lightgreen);

        final JPanel centralBottomPanel = new JPanel(new BorderLayout());
        centralBottomPanel.setBackground(lightgreen);

        // Discard pile in the center, Deck on the left
        centralBottomPanel.add(discardComponent, BorderLayout.CENTER);
        centralBottomPanel.add(deckView, BorderLayout.WEST);

        /*
         * Layout structure:
         * NORTH: The group containing Deck and Discard pile
         * CENTER: The area dedicated to the player's hand cards
         */
        this.add(centralBottomPanel, BorderLayout.NORTH);
        this.add(deckPanel, BorderLayout.CENTER); 
    }
}
