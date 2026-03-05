package view.hand;

import model.card.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import core.selectioncard.SelectionCardManager;


public class handImpl extends JPanel implements hand {

    private final SelectionCardManager selectionManager;
    private CardSelectionListener listener;

    public handImpl(SelectionCardManager selectionManager) {
    super(new FlowLayout(FlowLayout.LEFT, 5, 5));
    this.selectionManager = selectionManager;
    setBackground(new Color(245, 245, 245));
}

    @Override
    public void refreshHand(List<Card> hand) {
        this.removeAll();

        for (Card c : hand) {
            String cardText = c.toString();
            JButton btn = new JButton(cardText);
            
            // --- LOGICA COLORE SEMI ---
            if (cardText.contains("♥") || cardText.contains("♦")) {
                btn.setForeground(Color.RED);
            } else {
                btn.setForeground(Color.BLACK);
            }
            // ---------------------------

            btn.setPreferredSize(new Dimension(50, 70)); // Leggermente più grandi per leggibilità
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setOpaque(true);
            
            // Sfondo Giallo se selezionata, Bianco altrimenti
            btn.setBackground(selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));

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

    public Card getSingleSelectedCard() {
    Set<Card> selected = getSelectedCards();
    return selected.size() == 1 ? selected.iterator().next() : null;
    }

    public void updateHand(List<Card> hand) {
    refreshHand(hand);
    selectionManager.clearSelection();
    }
}