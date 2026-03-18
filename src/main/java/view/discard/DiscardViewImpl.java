package view.discard;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import model.card.*;

import java.awt.*;

/**
 * Concrete implementation of DiscardView.
 *
 * This class handles the graphical representation
 * of the discard pile and the discard button.
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
     * @param discardPanel the panel where discarded cards are shown
     * @param actionPanel the panel where the discard button is placed
     */
    public DiscardViewImpl(JPanel discardPanel, JPanel actionPanel) {
        this.discardPanel = discardPanel;
        this.actionPanel = actionPanel;

        
        this.discardPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.discardPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.WHITE, 1), 
        "Discard Pile", 
        0, 0, 
        new Font("Arial", Font.BOLD, 20), 
        Color.BLACK
        ));

       this.discardPanel.setBackground(new Color(220, 250, 220));

        
        discardButton = new JButton("Discard");
        this.actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.actionPanel.add(discardButton);
    }

    /**
     * Refreshes the discard pile panel with the current cards.
     *
     * @param discardPile the list of cards currently in the discard pile
     */
    @Override
    public void updateDiscardPile(List<Card> discardPile) {
    discardPanel.removeAll();
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


        if (isJolly) {

            label.setFont(new Font("Segoe UI Symbol", Font.BOLD, 28)); 
            label.setForeground(new Color(219, 112, 147)); // Viola
        } else {
            label.setFont(new Font("Monospaced", Font.BOLD, 19));

            if (textToShow.contains("♥") || textToShow.contains("♦")) {
                label.setForeground(Color.RED);
            } else {
                label.setForeground(Color.BLACK);
            }
        }
        label.setPreferredSize(new Dimension(60, 85));
        discardPanel.add(label);
    }

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
     * Returns the action panel containing the discard button,
     * so it can be added to the main frame.
     */
    public JPanel getActionPanel() {
        return actionPanel;
    }
}
