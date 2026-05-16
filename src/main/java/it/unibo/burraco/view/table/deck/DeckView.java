package it.unibo.burraco.view.table.deck;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

/**
 * Swing panel representing the deck button in the game UI.
 * This class isolates the graphical representation of the draw pile
 * from the main game table layout.
 */
public final class DeckView extends JPanel {

    private static final long serialVersionUID = 1L; 
    private static final int FLOW_HGAP = 5;
    private static final int FLOW_VGAP = 5;
    private static final int FONT_SIZE = 18;
    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 100;
    private static final int BORDER_THICKNESS = 1;
    private static final int SHADOW_THICKNESS = 4;
    private static final String FONT_NAME = "Arial";

    private final JButton deckButton;

    /**
     * Constructs the DeckView and initializes the deck button.
     */
    public DeckView() {
        super(new FlowLayout(FlowLayout.CENTER, FLOW_HGAP, FLOW_VGAP));
        this.setOpaque(false);

        this.deckButton = new JButton("DECK");
        this.deckButton.setBackground(Color.WHITE);
        this.deckButton.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
        this.deckButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.deckButton.setFocusPainted(false);
        this.deckButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, BORDER_THICKNESS),
            BorderFactory.createMatteBorder(0, 0, SHADOW_THICKNESS, SHADOW_THICKNESS, Color.BLACK)
        ));

        this.add(deckButton);
    }

    /**
     * Returns the deck button so that external components can register listeners on it.
     *
     * @return the deck button instance
     */
    public JButton getDeckButton() {
        return this.deckButton;
    }
}
