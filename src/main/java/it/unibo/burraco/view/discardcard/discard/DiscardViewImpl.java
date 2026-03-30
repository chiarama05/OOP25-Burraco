package it.unibo.burraco.view.discardcard.discard;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import it.unibo.burraco.model.card.Card;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Concrete implementation of {@link DiscardView} using Swing components.
 * This class handles the rendering of cards in the discard pile and manages 
 * the "Discard" button layout.
 */
public class DiscardViewImpl implements DiscardView {

    private static final int GAP = 5;
    private static final int TITLE_FONT_SIZE = 20;
    private static final int CARD_WIDTH_STEP = 70;
    private static final int WIDTH_OFFSET = 20;
    private static final int PANEL_HEIGHT = 100;
    private static final int JOLLY_FONT_SIZE = 28;
    private static final int NORMAL_FONT_SIZE = 19;
    private static final int LABEL_PREF_WIDTH = 60;
    private static final int LABEL_PREF_HEIGHT = 85;

    private static final Color BG_COLOR = new Color(220, 250, 220);
    private static final Color JOLLY_COLOR = new Color(219, 112, 147);

    private final JPanel discardPanel;
    private final JPanel actionPanel;
    private final JButton discardButton;

   /**
     * Constructs the discard view.
     * 
     * @param discardPanel the external panel provided to display cards.
     * @param actionPanel the external panel provided to host the discard button.
     */
    public DiscardViewImpl(final JPanel discardPanel, final JPanel actionPanel) {
        this.discardPanel = discardPanel;
        this.actionPanel = actionPanel;

        // Layout and Border for the discard pile
        this.discardPanel.setLayout(new FlowLayout(FlowLayout.LEFT, GAP, GAP));
        this.discardPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1), 
            "Discard Pile", 
            0, 0, 
            new Font("Arial", Font.BOLD, TITLE_FONT_SIZE), 
            Color.BLACK
        ));
        this.discardPanel.setBackground(BG_COLOR);

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
    public void updateDiscardPile(final List<Card> discardPile) {
        discardPanel.removeAll();

        // Dynamically adjust panel width to ensure scrollability
        final int width = (discardPile.size() * CARD_WIDTH_STEP) + WIDTH_OFFSET;
        discardPanel.setPreferredSize(new Dimension(width, PANEL_HEIGHT));

        for (final Card c : discardPile) {
            final boolean isJolly = c.getValue().equalsIgnoreCase("Jolly");
            final String textToShow = isJolly ? c.getSeed() : c.toString();

            final JLabel label = new JLabel(textToShow);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setHorizontalAlignment(JLabel.CENTER);

            // Joker vs Regular cards
            if (isJolly) {
                label.setFont(new Font("Segoe UI Symbol", Font.BOLD, JOLLY_FONT_SIZE)); 
                label.setForeground(JOLLY_COLOR); 
            } else {
                label.setFont(new Font("Monospaced", Font.BOLD, NORMAL_FONT_SIZE));
                // Red for Hearts and Diamonds
                if (textToShow.contains("♥") || textToShow.contains("♦")) {
                    label.setForeground(Color.RED);
                } else {
                    label.setForeground(Color.BLACK);
                }
            }
            label.setPreferredSize(new Dimension(LABEL_PREF_WIDTH, LABEL_PREF_HEIGHT));
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
    public void setDiscardListener(final ActionListener listener) {
        this.discardButton.addActionListener(listener);
    }

    /**
     * Provides access to the action panel for layout assembly in the main Frame.
     * @return the JPanel containing the discard button.
     */
    public JPanel getActionPanel() {
        return this.actionPanel;
    }
}
