package view.button;

import model.card.Card;
import model.player.Player;
import core.combination.AttachUtils;
import core.combination.StraightUtils;
import view.burraco.BurracoStyleManager;
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
    private final boolean isPlayer1Owner;

    public AttachedButton(List<Card> initialCards, TableViewImpl tableView, boolean isPlayer1Owner) {
        this.cards = new ArrayList<>(initialCards);
        this.tableView = tableView;
        this.isPlayer1Owner = isPlayer1Owner;

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
        if (!tableView.getDrawManager().hasDrawn()) {
        JOptionPane.showMessageDialog(this, "You have to draw from the deck or from the discard pile first!");
        return;
    }
        Player currentPlayer = tableView.getCurrentPlayer();

        boolean isCurrentPlayer1 = tableView.isPlayer1(currentPlayer);
        if (isCurrentPlayer1 != isPlayer1Owner) {
           JOptionPane.showMessageDialog(this, "You can only attach cards to your own combinations!");
           return;
        }
        
        List<Card> selected = tableView.getSelectionManager().getSelectedCards().stream()
                .filter(currentPlayer::hasCard)
                .collect(Collectors.toList());

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select the card from your hand first!");
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
            int sizeBefore = this.cards.size();
            this.cards.addAll(selected);
            currentPlayer.removeCards(selected);

            if (sizeBefore < 7 && this.cards.size() >= 7) {
               tableView.getSoundController().playBurracoSound();
            }
            
            updateVisuals(); 
            tableView.getSelectionManager().clearSelection();
            tableView.refreshHandPanel(currentPlayer);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid combination for this stack!");
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

        this.setBorder(BorderFactory.createCompoundBorder(
            BurracoStyleManager.getBurracoBorder(cards),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        this.setBackground(BurracoStyleManager.getBurracoBackground(cards));

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
