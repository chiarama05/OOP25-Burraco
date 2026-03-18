package view.button;

import model.card.Card;
import model.player.Player;
import core.buttonLogic.AttachController;
import core.buttonLogic.*;
import core.combination.CombinationValidator;
import core.combination.StraightUtils;
import core.controller.GameController;
import view.burraco.BurracoStyleManager;
import view.table.TableView;
import core.closure.ClosureManager;
import core.closure.ClosureState;
import core.closure.ClosureValidator;
import core.pot.PotManager;

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

            //// Hand empty, pot NOT yet taken → pot on fly
            case ZERO_CARDS_NO_POT:
                potManager.handlePot(false);
                break;

            //// Hand empty, pot taken, burraco present → round ends
            case CAN_CLOSE:
                closureManager.handleStateAfterAction(currentPlayer);
                break;

            //// Safety net (should be caught by preventive check above).
            case ZERO_CARDS_NO_BURRACO:
                closureManager.handleStateAfterAction(currentPlayer);
                tableView.refreshHandPanel(currentPlayer);
                break;

            // Normal case: player still has cards
            default:
                tableView.refreshHandPanel(currentPlayer);
                break;
        }
        



        // ## version 3 
        /*// Hand empty + pot taken + burraco → round ends immediately
        if (state == ClosureState.CAN_CLOSE) {
            closureManager.handleStateAfterAction(currentPlayer);
            return;
        }


        // Hand empty + pot taken + NO burraco yet.
        if (state == ClosureState.ZERO_CARDS_NO_BURRACO) {
            closureManager.handleStateAfterAction(currentPlayer);
            tableView.refreshHandPanel(currentPlayer);
            return;
        }

        // Normal case (OK) or any other state: just refresh.
        tableView.refreshHandPanel(currentPlayer);*/
        
        
    
        // ##
        /*if (success) {
            
            if (sizeBefore < 7 && this.cards.size() >= 7) {
                gameController.getSoundController().playBurracoSound();
            }
            
            updateVisuals(); 
            tableView.getHandViewForPlayer(currentPlayer).clearSelection();

            //normale case: when player still has cards 
            boolean blocking = closureManager.handleStateAfterAction(currentPlayer);

            if(!blocking){
                tableView.refreshHandPanel(currentPlayer);
            } 
        } 
        else {
            JOptionPane.showMessageDialog(this, "These cards cannot be attached!");
        }*/
    }


    // ##
    //conflicts with two updateVisuals --> this is the oldest version
    /*public void updateVisuals() {
        this.removeAll();
        
     
        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered);
            cards.clear(); 
            cards.addAll(ordered);
        } 
        else {
            cards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

    if (!gameController.getDrawManager().hasDrawn()) {
        JOptionPane.showMessageDialog(this, "Devi prima pescare!");
        return;
    }

    Player currentPlayer = gameController.getCurrentPlayer();
    if (gameController.isPlayer1(currentPlayer) != isPlayer1Owner) {
        JOptionPane.showMessageDialog(this, "Puoi attaccare carte solo alle tue combinazioni!");
        return;
    }

    List<Card> selected = new ArrayList<>(tableView.getHandViewForPlayer(currentPlayer).getSelectedCards());
    if (selected.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Seleziona prima le carte dalla mano!");
        return;
    }

    // --- PROTEZIONE CRITICA ---
    // Creiamo una lista ipotetica che unisce le carte già a terra e quelle selezionate
    List<Card> hypotheticalResult = new ArrayList<>(this.cards);
    hypotheticalResult.addAll(selected);

    // Chiediamo al Validator: "Se unissi queste carte, la combinazione sarebbe ancora valida?"
    // Questo blocca il Jolly se il 2 è costretto a diventare matta nella nuova configurazione.
    if (!CombinationValidator.isValidCombination(hypotheticalResult)) {
        JOptionPane.showMessageDialog(this, "Mossa non valida: troppe matte o scala interrotta!");
        return;
    }
    // ---------------------------

    int sizeBefore = this.cards.size();
    
    // Se la simulazione è passata, eseguiamo l'attacco vero e proprio
    boolean success = attachHandler.executeAttach(currentPlayer, selected, this.cards);

    if (success) {
        // Controllo Burraco (non cambia nulla, la logica rimane intatta)
        if (sizeBefore < 7 && this.cards.size() >= 7) {
            gameController.getSoundController().playBurracoSound();
        }
        
        updateVisuals(); 
        tableView.getHandViewForPlayer(currentPlayer).clearSelection();
        tableView.refreshHandPanel(currentPlayer);
    } else {
        JOptionPane.showMessageDialog(this, "Queste carte non possono essere attaccate!");
    }
}*/

    public void updateVisuals() {
    this.removeAll();
    
    // DEBUG: what arrives to this method
    System.out.println("DEBUG - CARTE RICEVUTE: " + cards);

    if (StraightUtils.isSameSeed(cards)) {
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
        Collections.reverse(ordered); 
        
        cards.clear();
        cards.addAll(ordered);
        
        System.out.println("DEBUG - ORDINAMENTO SCALA: " + cards);
    }
     else {
        //if it enters here the program thinks that it is a set and 2 will be the last one
        cards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        
        System.out.println("DEBUG - ORDINAMENTO SET (IL 2 SCIVOLA): " + cards);
    }

        // 2. Eastethic Seteup (border and background)
        this.setBorder(BorderFactory.createCompoundBorder(BurracoStyleManager.getBurracoBorder(cards),BorderFactory.createEmptyBorder(10, 5, 10, 5)));
        this.setBackground(BurracoStyleManager.getBurracoBackground(cards));


        for (Card c : cards) {
            renderCardLabel(c);
        }

        this.revalidate();
        this.repaint();

        // ## 
        /*BorderFactory.createEmptyBorder(10, 5, 10, 5);
        this.setBackground(BurracoStyleManager.getBurracoBackground(cards));

        // 3. Creation of graphic labels
        for (Card c : cards) {
            renderCardLabel(c);
        }

        this.revalidate();
        this.repaint(); */
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