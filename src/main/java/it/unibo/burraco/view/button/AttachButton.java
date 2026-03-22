package it.unibo.burraco.view.button;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.core.buttonLogic.AttachController;
import it.unibo.burraco.core.combination.CombinationValidator;
import it.unibo.burraco.core.combination.StraightUtils;
import it.unibo.burraco.core.controller.GameController;
import it.unibo.burraco.view.burraco.BurracoStyleManager;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.core.closure.ClosureManager;
import it.unibo.burraco.core.closure.ClosureState;
import it.unibo.burraco.core.closure.ClosureValidator;
import it.unibo.burraco.core.pot.PotManager;

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
    private final ClosureManager closureManager;
    private final PotManager potManager;

    public AttachButton(List<Card> initialCards, TableView tableView, GameController gameController, boolean isPlayer1Owner, ClosureManager closureManager, PotManager potManager) {
        this.cards = initialCards;
        this.tableView = tableView;
        this.gameController = gameController; 
        this.isPlayer1Owner = isPlayer1Owner;
        this.attachHandler = gameController.getAttachController();
        this.closureManager=closureManager;
        this.potManager=potManager;

        // Setup Estetico
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1),BorderFactory.createEmptyBorder(10, 5, 10, 5)));

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

        //verify that the combination is globally valid (it's block errors like differents seed's attach or to much wildcards on a scale)
        List<Card> hypotheticalResult = new ArrayList<>(this.cards);
        hypotheticalResult.addAll(selected);

        if (StraightUtils.isSameSeed(this.cards)) {
   
        hypotheticalResult = StraightUtils.orderStraight(hypotheticalResult);
        } else {
        hypotheticalResult.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

        System.out.println("DEBUG - Validating Hypothetical: " + hypotheticalResult);
        
        if (!CombinationValidator.isValidCombination(hypotheticalResult)) {
            JOptionPane.showMessageDialog(this,"Invalid move: the resulting combination would not be valid!\n"+ "(wrong suit, too many wildcards, or broken sequence)","Move Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }


        //Simulate the attach outcome before touching the model.
        if (ClosureValidator.wouldGetStuckAfterAttach(currentPlayer, selected, this.cards.size())) {
            JOptionPane.showMessageDialog(this,"You cannot attach this card!\n\n"+ "After attaching you would have only 1 card left,\n"+ "but you don't have a Burraco yet and you cannot close.\n\n"+ "You need at least one Burraco before you can reduce\n"+ "your hand to 1 card.","Move Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }



        int sizeBefore = this.cards.size();
        boolean success = attachHandler.executeAttach(currentPlayer, selected, this.cards);


        if (!success) {
            JOptionPane.showMessageDialog(this, "These cards cannot be attached!");
            return;
        }

        // Play burraco sound if this attach completed a burraco
        if (sizeBefore < 7 && this.cards.size() >= 7) {
            gameController.getSoundController().playBurracoSound();
        }

        updateVisuals();
        tableView.getHandViewForPlayer(currentPlayer).clearSelection();

        
        ClosureState state = ClosureValidator.evaluate(currentPlayer);

        
        switch (state) {

            // Hand empty, pot NOT yet taken → pot on fly
            case ZERO_CARDS_NO_POT:
                potManager.handlePot(false);
                break;

            // Hand empty, pot taken, burraco present → round ends
            case CAN_CLOSE:
                closureManager.handleStateAfterAction(currentPlayer);
                break;

            // Safety net (should be caught by preventive check above).
            case ZERO_CARDS_NO_BURRACO:
                closureManager.handleStateAfterAction(currentPlayer);
                tableView.refreshHandPanel(currentPlayer);
                break;

            // Normal case: player still has cards
            default:
                tableView.refreshHandPanel(currentPlayer);
                break;
        }
    }


    public void updateVisuals() {
        this.removeAll();

        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered); 
            
            cards.clear();
            cards.addAll(ordered);

        }
        else {
            //if it enters here the program thinks that it is a set and 2 will be the last one
            cards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));

        }

        // 2. Eastethic Seteup (border and background)
        this.setBorder(BorderFactory.createCompoundBorder(BurracoStyleManager.getBurracoBorder(cards),BorderFactory.createEmptyBorder(10, 5, 10, 5)));
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
            label.setForeground(new Color(219, 112, 147));
        } 
        else {
            label.setFont(new Font("Monospaced", Font.BOLD, 22));
            label.setForeground(c.toString().contains("♥") || c.toString().contains("♦") ? Color.RED : Color.BLACK);
        }

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(8));
    }
}