package it.unibo.burraco.view.discardcard.discard;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import it.unibo.burraco.model.card.*;

import java.awt.*;

/**
 * Concrete implementation of {@link DiscardView} using Swing components.
 * This class handles the rendering of cards in the discard pile and manages 
 * the "Discard" button layout.
 */
public class DiscardViewImpl implements DiscardView{

    /** Panel displaying the discard pile */
    private final JPanel discardPanel;

    /** Panel containing the discard button */
    private final JPanel actionPanel;

    /** Button used to discard the selected card */
    private final JButton discardButton;

   /**
     * Constructs the discard view.
     * 
     * @param discardPanel the external panel provided to display cards.
     * @param actionPanel the external panel provided to host the discard button.
     */
    public DiscardViewImpl(JPanel discardPanel, JPanel actionPanel) {
        this.discardPanel = discardPanel;
        this.actionPanel = actionPanel;

        // Layout and Border for the discard pile
        this.discardPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.discardPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1), 
            "Discard Pile", 
            0, 0, 
            new Font("Arial", Font.BOLD, 20), 
            Color.BLACK
        ));
        this.discardPanel.setBackground(new Color(220, 250, 220));

        // Discard Button initialization
        discardButton = new JButton("Discard");
        this.actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.actionPanel.add(discardButton);
    }

    /**
     * Refreshes the discard pile panel.
     * Removes old labels and generates new ones based on the provided list of cards.
     * Includes logic for card styling (color and font).
     *
     * @param discardPile the list of cards currently in the discard pile.
     */
    @Override
    public void updateDiscardPile(List<Card> discardPile) {
        discardPanel.removeAll();

        // Dynamically adjust panel width to ensure scrollability
        int width = (discardPile.size() * 70) + 20; 
        discardPanel.setPreferredSize(new Dimension(width, 100));

        for (Card c : discardPile) {
            boolean isJolly = c.getValue().equalsIgnoreCase("Jolly");
            String textToShow = isJolly ? c.getSeed() : c.toString();

            JLabel label = new JLabel(textToShow);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setHorizontalAlignment(JLabel.CENTER);

            // Joker vs Regular cards
            if (isJolly) {
                label.setFont(new Font("Segoe UI Symbol", Font.BOLD, 28)); 
                label.setForeground(new Color(219, 112, 147)); // Viola
            } else {
                label.setFont(new Font("Monospaced", Font.BOLD, 19));
                // Red for Hearts and Diamonds
                if (textToShow.contains("♥") || textToShow.contains("♦")) {
                    label.setForeground(Color.RED);
                } else {
                    label.setForeground(Color.BLACK);
                }
            }
            label.setPreferredSize(new Dimension(60, 85));
            discardPanel.add(label);
        }
        // Notify Swing to redraw the UI with new components
        discardPanel.revalidate();
        discardPanel.repaint();
    }


    /**
     * Attaches a listener to the discard button.
     *
     * @param listener the ActionListener triggered when the discard button is pressed
     */
    @Override
    public void setDiscardListener(ActionListener listener) {
        discardButton.addActionListener(listener);
    }

    /**
     * Provides access to the action panel for layout assembly in the main Frame.
     * @return the JPanel containing the discard button.
     */
    public JPanel getActionPanel() {
        return actionPanel;
    }
}