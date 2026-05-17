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
 * A button that displays a combination of cards and fires an {@link AttachListener}
 * when clicked.
 *
 * <p>Card value and seed checks now use {@link it.unibo.burraco.model.cards.CardValue}
 * and {@link it.unibo.burraco.model.cards.Seed} enum predicates instead of raw strings.
 */
@SuppressFBWarnings("Se")
public final class AttachButton extends JButton {

    private static final long serialVersionUID = 1L;

    private static final int FIXED_WIDTH = 64;
    private static final int VERTICAL_STRUT = 8;
    private static final int FONT_SIZE_JOLLY = 28;
    private static final int FONT_SIZE_NORMAL = 22;
    private static final int GAP = 5;
    private static final int BORDER_PADDING = 10;
    private static final int LINE_THICKNESS = 1;

    private static final Color JOLLY_COLOR =
            new Color(219, 112, 147);

    private final transient List<Card> cards;
    private final boolean isPlayer1Owner;

    /**
     * Constructs an AttachButton displaying the given combination.
     *
     * @param initialCards    the cards forming the combination
     * @param isPlayer1Owner  true if the combination belongs to Player 1
     * @param listener        the callback invoked when this button is clicked
     */
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

    /**
     * Refreshes the button's border, background and card labels.
     */
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

    /**
     * Renders a single card as a {@link JLabel} inside the button.
     * Jollies are shown as their seed symbol in pink; all other cards
     * use their standard string representation with red for Hearts/Diamonds.
     *
     * @param c the card to render
     */
    private void renderCardLabel(final Card c) {
        final boolean isJolly = c.getValue().isJolly();
        final String text = isJolly ? c.getSeed().getSymbol() : c.toString();

        final JLabel label = new JLabel(text);

        if (isJolly) {
            label.setFont(new Font("Segoe UI Symbol", Font.BOLD, FONT_SIZE_JOLLY));
            label.setForeground(JOLLY_COLOR);
        } else {
            label.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE_NORMAL));
            label.setForeground(c.getSeed().isRed() ? Color.RED : Color.BLACK);
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

    /**
     * Returns a defensive copy of the cards displayed by this button.
     *
     * @return the cards in this combination
     */
    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    /**
     * @return true if this combination belongs to Player 1
     */
    public boolean isPlayer1Owner() {
        return this.isPlayer1Owner;
    }
}
