package view.hand;

import model.card.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import core.selectioncard.SelectionCardManager;


public class handImpl extends JPanel implements hand {

    private final SelectionCardManager selectionManager = new SelectionCardManager();
    private CardSelectionListener listener;

    public handImpl() {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Mano"));
        setBackground(new Color(245, 245, 245));
    }

    @Override
    public void refreshHand(List<Card> hand) {
        removeAll();

    for (Card c : hand) {
        JButton btn = new JButton(c.toString());
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setOpaque(true);
        btn.setBackground(selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Listener aggiornato
        btn.addActionListener(e -> {
            // toggla selezione tramite SelectionCardManager
            selectionManager.toggleSelection(c);

            // Aggiorna solo il colore del bottone cliccato
            btn.setBackground(selectionManager.isSelected(c) ? Color.YELLOW : Color.WHITE);

            // callback verso controller
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
}