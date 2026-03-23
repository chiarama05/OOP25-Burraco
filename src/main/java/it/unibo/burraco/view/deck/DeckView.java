package it.unibo.burraco.view.deck;

import javax.swing.*;
import java.awt.*;

public class DeckView extends JPanel {
    private final JButton deckButton;

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

    public JButton getDeckButton() {
        return deckButton;
    }
}