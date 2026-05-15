package it.unibo.burraco.view.components.attach;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.view.components.BurracoStyleManager;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Constructs an AttachButton displaying the given combination.
 *
 * @param initialCards  the cards forming the combination
 * @param isPlayer1Owner true if the combination belongs to Player 1
 * @param listener      the callback invoked when this button is clicked
 */
@SuppressFBWarnings("Se")
public final class AttachButton extends JButton {
    
    private static final long serialVersionUID = 1L;
    private static final int FIXED_WIDTH = 64;
    private static final int VERTICAL_STRUT = 8;
    private static final int FONT_SIZE_JOLLY = 28;
    private static final int FONT_SIZE_NORMAL = 22;
    private static final int COLOR_JOLLY_R = 219;
    private static final int COLOR_JOLLY_G = 112;
    private static final int COLOR_JOLLY_B = 147;
    private static final int GAP = 5;
    private static final int BORDER_PADDING = 10;
    private static final int LINE_THICKNESS = 1;
    private static final String JOLLY_VALUE = "Jolly";

    private final transient List<Card> cards;
    private final boolean isPlayer1Owner;

    public AttachButton(final List<Card> initialCards,
                        final boolean isPlayer1Owner,
                        final AttachListener listener) {
        this.cards = new ArrayList<>(initialCards);
        this.isPlayer1Owner = isPlayer1Owner;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, LINE_THICKNESS),
                BorderFactory.createEmptyBorder(BORDER_PADDING, GAP, BORDER_PADDING, GAP)));
        this.updateVisuals();
        this.addActionListener(e -> listener.onAttachRequested(new ArrayList<>(this.cards)));
    }

    public void updateVisuals() {
    this.removeAll();

    this.setBorder(BorderFactory.createCompoundBorder(
            BurracoStyleManager.getBurracoBorder(this.cards),
            BorderFactory.createEmptyBorder(BORDER_PADDING, GAP, BORDER_PADDING, GAP)));
    this.setBackground(BurracoStyleManager.getBurracoBackground(this.cards));

    for (final Card c : this.cards) {
        renderCardLabel(c);
    }
    this.revalidate();
    this.repaint();
}

    private void renderCardLabel(final Card c) {
        final boolean isJolly = JOLLY_VALUE.equalsIgnoreCase(c.getValue());
        final JLabel label = new JLabel(isJolly ? c.getSeed() : c.toString());

        if (isJolly) {
            label.setFont(new Font("Segoe UI Symbol", Font.BOLD, FONT_SIZE_JOLLY));
            label.setForeground(new Color(COLOR_JOLLY_R, COLOR_JOLLY_G, COLOR_JOLLY_B));
        } else {
            label.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE_NORMAL));
            final String s = c.toString();
            label.setForeground(
                (s.contains("♥") || s.contains("♦")) ? Color.RED : Color.BLACK);
        }
        label.setAlignmentX(CENTER_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(FIXED_WIDTH, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(FIXED_WIDTH, super.getPreferredSize().height);
    }
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(FIXED_WIDTH, super.getMinimumSize().height);
    }

    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    public boolean isPlayer1Owner() {
        return this.isPlayer1Owner;
    }
}