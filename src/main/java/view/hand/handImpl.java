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
    setBackground(new Color(180, 220, 180));
}

    @Override
    public void refreshHand(List<Card> hand) {
    this.removeAll();

    for (Card c : hand) {
 
    boolean isJolly = c.getValue().equalsIgnoreCase("Jolly");
    

    String displayField = isJolly ? c.getSeed() : c.toString();
    
    JButton btn = new JButton(displayField);

    if (isJolly) {
        // --- STILE CORONA JOLLY ---
        btn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 27)); 
        btn.setForeground(new Color(219, 112, 147)); 
    } else {
        btn.setFont(new Font("Monospaced", Font.BOLD, 19));
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