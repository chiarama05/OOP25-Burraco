package view.discard;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionListener;
import model.card.*;

/**
 * Concrete implementation of DiscardView.
 *
 * This class handles the graphical representation
 * of the discard pile and the discard button.
 */
public class DiscardViewImpl implements DiscardView{

    /** Panel displaying the discard pile */
    private final JPanel discardPanel;

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

        discardButton = new JButton("Discard");
        actionPanel.add(discardButton);
    }

    /**
     * Refreshes the discard pile panel.
     */
    @Override
    public void updateDiscardPile(List<Card> discardPile) {

        discardPanel.removeAll();
        for (Card c : discardPile) {
            JLabel label = new JLabel(c.toString());
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            discardPanel.add(label);
        }

        discardPanel.revalidate();
        discardPanel.repaint();
    }

    /**
     * Attaches a listener to the discard button.
     */
    @Override
    public void setDiscardListener(ActionListener listener) {
        discardButton.addActionListener(listener);
    }
}
