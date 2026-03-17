package view.button;

import model.card.Card;
import model.player.Player;
import core.combination.AttachUtils;
import core.combination.StraightUtils;
import view.burraco.BurracoStyleManager;
import view.controller.GameController; 
import view.table.TableView;
import view.table.TableViewImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AttachedButton extends JButton {

    private final List<Card> cards;
    private final TableView tableView;
    private final GameController gameController; 
    private final boolean isPlayer1Owner;

    public AttachedButton(List<Card> initialCards, TableView tableView, GameController gameController, boolean isPlayer1Owner) {
        this.cards = new ArrayList<>(initialCards);
        this.tableView = tableView;
        this.gameController = gameController; 
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
        
        if (!gameController.getDrawManager().hasDrawn()) {
        JOptionPane.showMessageDialog(this, "You have to draw first!");
        return;
        }

        // CHIEDI AL GAMECONTROLLER
        Player currentPlayer = gameController.getCurrentPlayer();

        java.util.Set<Card> selectedSet = tableView.getHandViewForPlayer(currentPlayer).getSelectedCards();
        
        
        List<Card> selected = new ArrayList<>(selectedSet);
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select the card from your hand first!");
            return;
        }

        boolean isCurrentPlayer1 = gameController.isPlayer1(currentPlayer);
        if (isCurrentPlayer1 != isPlayer1Owner) {
            JOptionPane.showMessageDialog(this, "You can only attach cards to your own combinations!");
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

            // Sincronizzazione con il modello del player
            for (List<Card> playerComb : currentPlayer.getCombinations()) {
                if (!this.cards.isEmpty() && playerComb.contains(this.cards.get(0))) {
                    playerComb.clear();
                    playerComb.addAll(this.cards);
                    break;
                }
            }
            
            currentPlayer.removeCards(selected);

            // AUDIO DAL GAMECONTROLLER
            if (sizeBefore < 7 && this.cards.size() >= 7) {
               gameController.getSoundController().playBurracoSound();
            }
            
            updateVisuals(); 
            tableView.getHandViewForPlayer(currentPlayer).clearSelection();
            tableView.refreshHandPanel(currentPlayer);
        }else {
            JOptionPane.showMessageDialog(this, "These cards cannot be attached to this combination!");
        }
    }

    public void updateVisuals() {
        this.removeAll();

        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(cards);
            Collections.reverse(ordered); 
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
            boolean isJolly = c.getValue().equalsIgnoreCase("Jolly");
            String textToShow = isJolly ? c.getSeed() : c.toString();
            JLabel label = new JLabel(textToShow);
            if (isJolly) {
            // Usiamo un font che supporti bene i simboli e lo facciamo GRANDE
            label.setFont(new Font("Segoe UI Symbol", Font.BOLD, 28)); 
            label.setForeground(new Color(153, 0, 255)); // Viola Burraco
            } else {
            label.setFont(new Font("Monospaced", Font.BOLD, 22));
            label.setForeground(getCardColor(c.toString()));
            }
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.add(label);
            this.add(Box.createVerticalStrut(8));
        }

        this.revalidate();
        this.repaint();
    }

    private Color getCardColor(String cardString) {
        return (cardString.contains("♥") || cardString.contains("♦")) ? Color.RED : Color.BLACK;
    }
}
