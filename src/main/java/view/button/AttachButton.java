package view.button;

import model.card.Card;
import model.player.Player;
import core.buttonLogic.*;
import core.combination.CombinationValidator;
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
}

    public void updateVisuals() {
    this.removeAll();
    
    // DEBUG: Vediamo cosa arriva al metodo
    System.out.println("DEBUG - CARTE RICEVUTE: " + cards);

    // 1. Decidiamo come ordinare
    if (StraightUtils.isSameSeed(cards)) {
        // Se entra qui, il programma riconosce che è una SCALA
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
        Collections.reverse(ordered); // Invertiamo per avere 8, 7, 2, 5
        
        cards.clear();
        cards.addAll(ordered);
        
        System.out.println("DEBUG - ORDINAMENTO SCALA: " + cards);
    } else {
        // Se entra qui, il programma pensa sia un SET (Tris/Quartina)
        // E ordina per valore numerico: il 2 finirà SEMPRE per ultimo
        cards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        
        System.out.println("DEBUG - ORDINAMENTO SET (IL 2 SCIVOLA): " + cards);
    }

    // 2. Setup Estetico (Bordi e Sfondo)
    this.setBorder(BorderFactory.createCompoundBorder(
            BurracoStyleManager.getBurracoBorder(cards),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
    ));
    this.setBackground(BurracoStyleManager.getBurracoBackground(cards));

    // 3. Creazione delle etichette grafiche
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