package it.unibo.burraco.view.deck;

import javax.swing.*;
import java.awt.*;

/**
 * Swing panel representing the deck button in the game UI.
 */
public class DeckView extends JPanel {

    private final JButton deckButton;

    /**
     * Constructs the DeckView and initializes the deck button.
     */
    public DeckView() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setOpaque(false);

        deckButton = new JButton("DECK");
        deckButton.setBackground(Color.WHITE);
        deckButton.setFont(new Font("Arial", Font.BOLD, 18));
        deckButton.setPreferredSize(new Dimension(70, 100));
        deckButton.setFocusPainted(false);
        deckButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createMatteBorder(0, 0, 4, 4, Color.BLACK)
        ));

        add(deckButton);
    }

    /**
     * Returns the deck button so that external components can register listeners on it.
     *
     * @return the deck {@link JButton}
     */
    public JButton getDeckButton() {
        return deckButton;
    }
}