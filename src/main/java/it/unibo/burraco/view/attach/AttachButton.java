package it.unibo.burraco.view.attach;

import it.unibo.burraco.controller.attach.AttachActionController;
import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.burraco.BurracoStyleManager;
import it.unibo.burraco.view.notification.attach.AttachNotifier;
import it.unibo.burraco.view.notification.attach.AttachNotifierImpl;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.controller.combination.StraightUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttachButton extends JButton implements AttachView {

    private final List<Card> cards;
    private final TableView tableView;
    private final GameController gameController;
    private final AttachActionController actionController;

    public AttachButton(List<Card> initialCards, TableView tableView,
                        GameController gameController, boolean isPlayer1Owner,
                        ClosureManager closureManager, PotManager potManager, JFrame parentFrame) {
        this.cards = initialCards;
        this.tableView = tableView;
        this.gameController = gameController;

        AttachNotifier attachNotifier = new AttachNotifierImpl(
            SwingUtilities.getWindowAncestor(this) instanceof JFrame f ? f : null);

        this.actionController = new AttachActionController(
            gameController, potManager, closureManager, attachNotifier, isPlayer1Owner);

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
        actionController.handle(selected, this.cards, this);
    }

    // --- AttachView ---

    @Override
    public void showAttachError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void updateCombinationVisuals() {
        updateVisuals();
    }

    @Override
    public void onAttachSuccess(Player p) {
        tableView.getHandViewForPlayer(p).clearSelection();
        tableView.refreshHandPanel(p);
    }

    @Override
    public void onAttachTakePot(Player p) {
        tableView.getHandViewForPlayer(p).clearSelection();
    }

    @Override
    public void onAttachClose(Player p) {
        tableView.getHandViewForPlayer(p).clearSelection();
        tableView.refreshHandPanel(p);
    }

    // --- Rendering ---

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
