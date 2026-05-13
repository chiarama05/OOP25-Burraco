package it.unibo.burraco.view.components.attach;
 
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.rules.StraightUtils;
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
import java.util.Collections;
import java.util.List;
 
/**
 * A button representing a combination on the table.
 * When clicked, it fires an {@link AttachListener} with the combination's cards.
 *
 * This class has no knowledge of Move, CompletableFuture, or hand selection —
 * those concerns belong to the wiring layer (TableViewImpl).
 */
@SuppressFBWarnings("Se")
public final class AttachButton extends JButton {
 
    private static final long serialVersionUID = 1L;
    private static final int FIXED_WIDTH = 64;
    private static final int VERTICAL_STRUT_SIZE = 8;
    private static final int FONT_SIZE_JOLLY = 28;
    private static final int FONT_SIZE_NORMAL = 22;
    private static final int COLOR_JOLLY_R = 219;
    private static final int COLOR_JOLLY_G = 112;
    private static final int COLOR_JOLLY_B = 147;
    private static final int GAP = 5;
    private static final int BORDER_PADDING = 10;
    private static final int LINE_THICKNESS = 1;
    private static final String JOLLY_VALUE = "Jolly";
    private static final String TWO_VALUE = "2";
 
    private final transient List<Card> cards;
    private final boolean isPlayer1Owner;
    private final transient StraightUtils straightUtils;
 
    /**
     * Constructs an AttachButton.
     *
     * @param initialCards   the cards that form this combination
     * @param isPlayer1Owner true if this combination belongs to Player 1
     * @param listener       callback invoked when the button is clicked;
     *                       receives the combination's current card list
     */
    public AttachButton(final List<Card> initialCards,
                        final boolean isPlayer1Owner,
                        final AttachListener listener) {
        this.cards = new ArrayList<>(initialCards);
        this.isPlayer1Owner = isPlayer1Owner;
        this.straightUtils = new StraightUtils();
 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, LINE_THICKNESS),
                BorderFactory.createEmptyBorder(BORDER_PADDING, GAP, BORDER_PADDING, GAP)));
        this.updateVisuals();
 
        // The button notifies the listener with its cards.
        // It does not know about Move, hand views, or futures.
        this.addActionListener(e -> listener.onAttachRequested(new ArrayList<>(this.cards)));
    }
 
    /** Refreshes the visual representation based on the current card list. */
    public void updateVisuals() {
        this.removeAll();
 
        if (this.straightUtils.isSameSeed(cards)
                && this.straightUtils.isValidStraight(new ArrayList<>(cards))) {
            final List<Card> ordered = this.straightUtils.orderStraight(new ArrayList<>(this.cards));
            Collections.reverse(ordered);
            this.cards.clear();
            this.cards.addAll(ordered);
        } else {
            final String baseValue = cards.stream()
                .filter(c -> !JOLLY_VALUE.equalsIgnoreCase(c.getValue())
                        && !TWO_VALUE.equals(c.getValue()))
                .map(Card::getValue)
                .findFirst()
                .orElse(null);
 
            final List<Card> naturals = new ArrayList<>();
            final List<Card> wildcards = new ArrayList<>();
 
            for (final Card c : this.cards) {
                final boolean isWild = JOLLY_VALUE.equalsIgnoreCase(c.getValue())
                    || TWO_VALUE.equals(c.getValue()) && !TWO_VALUE.equals(baseValue);
                if (isWild) {
                    wildcards.add(c);
                } else {
                    naturals.add(c);
                }
            }
            naturals.sort((c1, c2) ->
                    Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
            this.cards.clear();
            this.cards.addAll(wildcards);
            this.cards.addAll(naturals);
        }
 
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
            final String cardStr = c.toString();
            if (cardStr.contains("♥") || cardStr.contains("♦")) {
                label.setForeground(Color.RED);
            } else {
                label.setForeground(Color.BLACK);
            }
        }
        label.setAlignmentX(CENTER_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SIZE));
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
 
    /** Returns the cards currently displayed in this combination button. */
    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }
 
    /** Returns true if this combination belongs to Player 1. */
    public boolean isPlayer1Owner() {
        return this.isPlayer1Owner;
    }
}