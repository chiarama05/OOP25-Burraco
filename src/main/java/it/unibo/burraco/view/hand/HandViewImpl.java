package it.unibo.burraco.view.hand;

import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link HandView}.
 * It displays cards as interactive {@link JButton} components within a panel,
 * handling visual feedback for selection, hover effects, and Jolly-specific styling.
 */
public final class HandViewImpl extends JPanel implements HandView {

    private static final long serialVersionUID = 1L;

    private static final int GAP = 5;
    private static final int CARD_WIDTH = 65;
    private static final int CARD_HEIGHT = 90;
    private static final int PREF_CARD_STEP = 70;
    private static final int PREF_WIDTH_OFFSET = 20;
    private static final int PANEL_HEIGHT = 105;

    private static final int JOLLY_FONT_SIZE = 27;
    private static final int NORMAL_FONT_SIZE = 19;
    private static final Color PANEL_BG = new Color(180, 220, 180);
    private static final Color JOLLY_COLOR = new Color(219, 112, 147);
    private static final Color HOVER_BG = new Color(255, 255, 225);
    private static final Color HOVER_BORDER = new Color(240, 230, 140);
    private static final int HOVER_BORDER_THICKNESS = 2;

    private static final int HOVER_OFFSET = 2;
    private static final String JOLLY_VALUE = "Jolly";
    private static final String JOLLY_FONT = "Segoe UI Symbol";
    private static final String NORMAL_FONT = "Monospaced";

    private final transient SelectionCardManager selectionManager;
    private transient CardSelectionListener cardSelectionListener;

    /**
     * Constructs a HandViewImpl with a specific selection manager.
     *
     * @param selectionManager the manager responsible for tracking selected cards
     */
    public HandViewImpl(final SelectionCardManager selectionManager) {
        super(new FlowLayout(FlowLayout.LEFT, GAP, GAP));
        this.selectionManager = selectionManager;
        this.setBackground(PANEL_BG);
    }

    @Override
    public void refreshHand(final List<Card> hand) {
        this.removeAll();

        final int preferredWidth = (hand.size() * PREF_CARD_STEP) + PREF_WIDTH_OFFSET;
        this.setPreferredSize(new Dimension(preferredWidth, PANEL_HEIGHT));

        for (final Card c : hand) {
            final boolean isJolly = JOLLY_VALUE.equalsIgnoreCase(c.getValue());
            final String displayField = isJolly ? c.getSeed() : c.toString();

            final JButton btn = new JButton(displayField);

            if (isJolly) {
                btn.setFont(new Font(JOLLY_FONT, Font.BOLD, JOLLY_FONT_SIZE));
                btn.setForeground(JOLLY_COLOR);
            } else {
                btn.setFont(new Font(NORMAL_FONT, Font.BOLD, NORMAL_FONT_SIZE));
                if (displayField.contains("♥") || displayField.contains("♦")) {
                    btn.setForeground(Color.RED);
                } else {
                    btn.setForeground(Color.BLACK);
                }
            }

            btn.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            btn.setOpaque(true);
            btn.setBackground(this.selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(final MouseEvent e) {
                    if (!selectionManager.isSelected(c)) {
                        btn.setBackground(HOVER_BG);
                    }
                    btn.setBorder(BorderFactory.createLineBorder(HOVER_BORDER, HOVER_BORDER_THICKNESS));
                    btn.setLocation(btn.getX(), btn.getY() - HOVER_OFFSET);
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    if (!selectionManager.isSelected(c)) {
                        btn.setBackground(Color.WHITE);
                    } else {
                        btn.setBackground(Color.YELLOW);
                    }
                    btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    btn.setLocation(btn.getX(), btn.getY() + HOVER_OFFSET);
                }
            });

            btn.addActionListener(e -> {
                this.selectionManager.toggleSelection(c);
                btn.setBackground(this.selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);
                if (this.cardSelectionListener != null) {
                    this.cardSelectionListener.onCardSelected(c);
                }
            });

            this.add(btn);
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public Set<Card> getSelectedCards() {
        return this.selectionManager.getSelectedCards();
    }

    @Override
    public void clearSelection() {
        this.selectionManager.clearSelection();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void setCardSelectionListener(final CardSelectionListener listener) {
        this.cardSelectionListener = listener;
    }

    /**
     * Helper method to retrieve a single selected card.
     *
     * @return the selected {@link Card} if exactly one is selected, null otherwise
     */
    public Card getSingleSelectedCard() {
        final Set<Card> selected = this.getSelectedCards();
        return selected.size() == 1 ? selected.iterator().next() : null;
    }

    /**
     * Updates the hand and clears the current selection.
     *
     * @param hand the new list of cards to display
     */
    public void updateHand(final List<Card> hand) {
        this.refreshHand(hand);
        this.selectionManager.clearSelection();
    }
}
