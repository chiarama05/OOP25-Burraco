package view.button;

import model.card.Card;
import model.player.Player;
import core.buttonLogic.*;
import core.combination.StraightUtils;
import core.controller.GameController;
import view.burraco.BurracoStyleManager;
import view.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttachButton extends JButton {

    private final List<Card> cards; 
    private final TableView tableView;
    private final GameController gameController; 
    private final boolean isPlayer1Owner;
    private final AttachController attachHandler; 

    public AttachButton(List<Card> initialCards, TableView tableView, GameController gameController, boolean isPlayer1Owner) {
        this.cards = initialCards;
        this.tableView = tableView;
        this.gameController = gameController; 
        this.isPlayer1Owner = isPlayer1Owner;
        this.attachHandler = gameController.getAttachController();

        // Setup Estetico
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));

        updateVisuals();
        this.addActionListener(e -> handleAttachAction());
    }

    private void handleAttachAction() {
       
        if (!gameController.getDrawManager().hasDrawn()) {
            JOptionPane.showMessageDialog(this, "You have to draw first!");
            return;
        }

        Player currentPlayer = gameController.getCurrentPlayer();
    
        if (gameController.isPlayer1(currentPlayer) != isPlayer1Owner) {
            JOptionPane.showMessageDialog(this, "You can only attach cards to your own combinations!");
            return;
        }

        List<Card> selected = new ArrayList<>(tableView.getHandViewForPlayer(currentPlayer).getSelectedCards());
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select the card from your hand first!");
            return;
        }

        
        int sizeBefore = this.cards.size();
        boolean success = attachHandler.executeAttach(currentPlayer, selected, this.cards);

        if (success) {
            
            if (sizeBefore < 7 && this.cards.size() >= 7) {
                gameController.getSoundController().playBurracoSound();
            }
            
            updateVisuals(); 
            tableView.getHandViewForPlayer(currentPlayer).clearSelection();
            tableView.refreshHandPanel(currentPlayer);
        } else {
            JOptionPane.showMessageDialog(this, "These cards cannot be attached!");
        }
    }

    public void updateVisuals() {
        this.removeAll();
        
     
        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered);
            cards.clear(); cards.addAll(ordered);
        } else {
            cards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

      
        this.setBorder(BorderFactory.createCompoundBorder(
            BurracoStyleManager.getBurracoBorder(cards),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        this.setBackground(BurracoStyleManager.getBurracoBackground(cards));

        for (Card c : cards) {
            renderCardLabel(c);
        }

        this.revalidate();
        this.repaint();
    }

    private void renderCardLabel(Card c) {
        boolean isJolly = c.getValue().equalsIgnoreCase("Jolly");
        JLabel label = new JLabel(isJolly ? c.getSeed() : c.toString());
        
        if (isJolly) {
            label.setFont(new Font("Segoe UI Symbol", Font.BOLD, 28)); 
            label.setForeground(new Color(153, 0, 255));
        } else {
            label.setFont(new Font("Monospaced", Font.BOLD, 22));
            label.setForeground(c.toString().contains("♥") || c.toString().contains("♦") ? Color.RED : Color.BLACK);
        }
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(8));
    }
}