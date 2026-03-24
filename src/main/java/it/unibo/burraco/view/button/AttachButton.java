package it.unibo.burraco.view.button;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.controller.combination.StraightUtils;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.view.burraco.BurracoStyleManager;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.controller.attach.AttachController;
import it.unibo.burraco.controller.attach.AttachResult;
import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.pot.PotManager;

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
    private final AttachController attachController;
    private final ClosureManager closureManager;
    private final PotManager potManager;

    public AttachButton(List<Card> initialCards, TableView tableView,
                        GameController gameController, boolean isPlayer1Owner,
                        ClosureManager closureManager, PotManager potManager) {
        this.cards = initialCards;
        this.tableView = tableView;
        this.gameController = gameController;
        this.isPlayer1Owner = isPlayer1Owner;
        this.attachController = gameController.getAttachController();
        this.closureManager = closureManager;
        this.potManager = potManager;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)));

        updateVisuals();
        this.addActionListener(e -> handleAttachAction());
    }

    private void handleAttachAction() {
        Player currentPlayer = gameController.getCurrentPlayer();
    List<Card> selected = new ArrayList<>(
            tableView.getHandViewForPlayer(currentPlayer).getSelectedCards());

    boolean hasDrawn = gameController.getDrawManager().hasDrawn();
    boolean isCurrentPlayer = (gameController.isPlayer1(currentPlayer) == isPlayer1Owner);

    AttachResult result = attachController.tryAttach(
            currentPlayer,
            selected,
            this.cards,
            hasDrawn,        
            isCurrentPlayer 
    );


     if (result == AttachResult.SUCCESS_BURRACO) {
        gameController.getSoundController().playBurracoSound();
    }

    switch (result) {

            case NOT_DRAWN ->
                JOptionPane.showMessageDialog(this, "You have to draw first!");

            case WRONG_PLAYER ->
                JOptionPane.showMessageDialog(this,
                        "You can only attach cards to your own combinations!");

            case NO_CARDS_SELECTED ->
                JOptionPane.showMessageDialog(this,
                        "Select the card from your hand first!");

            case INVALID_COMBINATION ->
                JOptionPane.showMessageDialog(this,
                        "Invalid move: the resulting combination would not be valid!\n"
                        + "(wrong suit, too many wildcards, or broken sequence)",
                        "Move Not Allowed", JOptionPane.WARNING_MESSAGE);

            case WOULD_GET_STUCK ->
                JOptionPane.showMessageDialog(this,
                        "You cannot attach this card!\n\n"
                        + "After attaching you would have only 1 card left,\n"
                        + "but you don't have a Burraco yet and you cannot close.\n\n"
                        + "You need at least one Burraco before you can reduce\n"
                        + "your hand to 1 card.",
                        "Move Not Allowed", JOptionPane.WARNING_MESSAGE);

            case ATTACH_FAILED ->
                JOptionPane.showMessageDialog(this,
                        "These cards cannot be attached!");

            // Tutti i casi di successo: aggiorna UI e gestisci transizioni
            case SUCCESS, SUCCESS_BURRACO -> {
                updateVisuals();
                tableView.getHandViewForPlayer(currentPlayer).clearSelection();
                tableView.refreshHandPanel(currentPlayer);
            }

            case SUCCESS_TAKE_POT -> {
                updateVisuals();
                tableView.getHandViewForPlayer(currentPlayer).clearSelection();
                potManager.handlePot(false);
            }

            case SUCCESS_CLOSE, SUCCESS_STUCK -> {
                updateVisuals();
                tableView.getHandViewForPlayer(currentPlayer).clearSelection();
                closureManager.handleStateAfterAction(currentPlayer);
                tableView.refreshHandPanel(currentPlayer);
            }
        }
    }

    public void updateVisuals() {
        this.removeAll();

        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered);
            cards.clear();
            cards.addAll(ordered);
        } else {
            cards.sort((c1, c2) ->
                    Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

        this.setBorder(BorderFactory.createCompoundBorder(
                BurracoStyleManager.getBurracoBorder(cards),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)));
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
        } else {
            label.setFont(new Font("Monospaced", Font.BOLD, 22));
            label.setForeground(
                    c.toString().contains("♥") || c.toString().contains("♦")
                    ? Color.RED : Color.BLACK);
        }

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(8));
    }
}  
