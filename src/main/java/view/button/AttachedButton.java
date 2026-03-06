package view.button;

import model.card.Card;
import model.player.Player;
import core.combination.AttachUtils;
import core.combination.StraightUtils;
import view.table.TableViewImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AttachedButton extends JButton {

    private final List<Card> cards;
    private final TableViewImpl tableView;

    public AttachedButton(List<Card> initialCards, TableViewImpl tableView) {
        this.cards = new ArrayList<>(initialCards);
        this.tableView = tableView;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));


        updateVisuals();


        this.addActionListener(e -> handleAttach());
    }

    private void handleAttach() {
        Player currentPlayer = tableView.getCurrentPlayer();
        
        List<Card> selected = tableView.getSelectionManager().getSelectedCards().stream()
                .filter(currentPlayer::hasCard)
                .collect(Collectors.toList());

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleziona prima le carte dalla mano!");
            return;
        }

       
        boolean canAttachAll = true;
        for (Card c : selected) {
            if (!AttachUtils.canAttach(this.cards, c)) {
                canAttachAll = false;
                break;
            }
        }

        if (canAttachAll) {
            this.cards.addAll(selected);
            currentPlayer.removeCards(selected);
            
            updateVisuals(); 
            tableView.getSelectionManager().clearSelection();
            tableView.refreshHandPanel(currentPlayer);
        } else {
            JOptionPane.showMessageDialog(this, "Combinazione non valida per questa pila!");
        }
    }

    public void updateVisuals() {
        this.removeAll();

        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(cards);
            Collections.reverse(ordered); // [K, Q, J...]
            cards.clear();
            cards.addAll(ordered);
        } else {
            cards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

        for (Card c : cards) {
            JLabel label = new JLabel(c.toString());
            label.setFont(new Font("Monospaced", Font.BOLD, 22));
            label.setForeground(getCardColor(c.toString()));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            this.add(label);
            this.add(Box.createVerticalStrut(8)); // Spazio tra le carte
        }

        this.revalidate();
        this.repaint();
    }

    private Color getCardColor(String cardString) {
        return (cardString.contains("♥") || cardString.contains("♦")) ? Color.RED : Color.BLACK;
    }
}
