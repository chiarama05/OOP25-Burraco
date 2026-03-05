package view.botton;

import javax.swing.*;
import java.awt.*;

public class DeckView extends JPanel {
    private final JButton deckButton;

    public DeckView() {
        // Layout fluido
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setOpaque(false);

        deckButton = new JButton("DECK");
        
        // OPZIONE A: Pulsante Bianco Minimalista
        deckButton.setBackground(Color.WHITE);
        deckButton.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Se hai un'immagine (es. "back.png" nella cartella resources) usa questa:
        /*
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/back.png"));
        Image img = icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
        deckButton.setIcon(new ImageIcon(img));
        */

        deckButton.setPreferredSize(new Dimension(80, 110));
        deckButton.setFocusPainted(false);
        
        // Bordo per dare profondità (effetto pila)
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