package it.unibo.burraco.view.hand;

import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link HandView}.
 * It displays cards as interactive {@link JButton} components within a panel,
 * handling visual feedback for selection, hover effects, and Jolly-specific styling.
 */
public final class HandViewImpl extends JPanel implements HandView {

    private static final long serialVersionUID = 1L;

    private final SelectionCardManager selectionManager;
    private CardSelectionListener listener;

    /**
     * Constructs a HandViewImpl with a specific selection manager.
     * @param selectionManager the manager responsible for tracking selected cards
     */
    public HandViewImpl(SelectionCardManager selectionManager) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.selectionManager = selectionManager;
        setBackground(new Color(180, 220, 180));
    }

    @Override
    public void refreshHand(List<Card> hand) {
        this.removeAll();

        // Dynamically adjust panel width based on the number of cards
        int preferredWidth = (hand.size() * 70) + 20;
        this.setPreferredSize(new Dimension(preferredWidth, 105));

        for (Card c : hand) {
            boolean isJolly = c.getValue().equalsIgnoreCase("Jolly");
            String displayField = isJolly ? c.getSeed() : c.toString();

            JButton btn = new JButton(displayField);

            // Apply specific styling for Jokers
            if (isJolly) {
                btn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 27)); 
                btn.setForeground(new Color(219, 112, 147)); 
            } else {
                btn.setFont(new Font("Monospaced", Font.BOLD, 19));
                // Set color based on the suit 
                if (displayField.contains("♥") || displayField.contains("♦")) {
                    btn.setForeground(Color.RED);
                } else {
                    btn.setForeground(Color.BLACK);
                }
            }

            btn.setPreferredSize(new Dimension(65, 90));
            btn.setOpaque(true);
            btn.setBackground(selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            // Add interactive hover effects
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!selectionManager.isSelected(c)) {
                        btn.setBackground(new Color(255, 255, 225)); 
                    }
                    btn.setBorder(BorderFactory.createLineBorder(new Color(240, 230, 140), 2)); 
                    btn.setLocation(btn.getX(), btn.getY() - 2);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!selectionManager.isSelected(c)) {
                        btn.setBackground(Color.WHITE);
                    } else {
                        btn.setBackground(Color.YELLOW);
                    }
                    btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    btn.setLocation(btn.getX(), btn.getY() + 2);
                }
            });
        
            btn.addActionListener(e -> {
                selectionManager.toggleSelection(c);
                btn.setBackground(selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);
                if (listener != null) {
                    listener.onCardSelected(c);
                }
            });

            add(btn);
        }
        revalidate();
        repaint();
    }    

    @Override
    public Set<Card> getSelectedCards() {
        return selectionManager.getSelectedCards();
    }

    @Override
    public void clearSelection() {
        selectionManager.clearSelection();
        revalidate();
        repaint();
    }

    @Override
    public void setCardSelectionListener(CardSelectionListener listener) {
        this.listener = listener;
    }

    /**
     * Helper method to retrieve a single selected card.
     * @return the selected {@link Card} if exactly one is selected, null otherwise
     */
    public Card getSingleSelectedCard() {
        Set<Card> selected = getSelectedCards();
        return selected.size() == 1 ? selected.iterator().next() : null;
    }

    /**
     * Updates the hand and clears the current selection.
     * @param hand the new list of cards to display
     */
    public void updateHand(List<Card> hand) {
        refreshHand(hand);
        selectionManager.clearSelection();
    }
}