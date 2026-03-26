package it.unibo.burraco.view.attach;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.burraco.BurracoStyleManager;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.controller.combination.StraightUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class AttachButton extends JButton implements AttachView {

    private final List<Card> cards;
    private final TableView tableView;
    private final boolean isPlayer1Owner;
    private static final int FIXED_WIDTH = 56;

    private BiConsumer<List<Card>, AttachButton> onAttachAction;

    public AttachButton(List<Card> initialCards, TableView tableView, boolean isPlayer1Owner) {
        this.cards = initialCards;
        this.tableView = tableView;
        this.isPlayer1Owner = isPlayer1Owner;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)));

        updateVisuals();
        this.addActionListener(e -> handleAttachAction());
    }

    public void setOnAttachAction(BiConsumer<List<Card>, AttachButton> handler) {
        this.onAttachAction = handler;
    }

    private void handleAttachAction() {
        if (onAttachAction == null) return;
        List<Card> selected = new ArrayList<>(
                tableView.getHandViewForCurrentPlayer(isPlayer1Owner).getSelectedCards());
        onAttachAction.accept(selected, this);
    }

    @Override
    public void showAttachError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void updateCombinationVisuals() {
        updateVisuals();
    }

    @Override
    public void onAttachSuccess(Player p, boolean isPlayer1) {
        tableView.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
        tableView.refreshHandPanel(isPlayer1, p.getHand());
    }

    @Override
    public void onAttachTakePot(Player p, boolean isPlayer1) {
        tableView.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
    }

    @Override
    public void onAttachClose(Player p, boolean isPlayer1) {
        tableView.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
        tableView.refreshHandPanel(isPlayer1, p.getHand());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(FIXED_WIDTH, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(FIXED_WIDTH, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(FIXED_WIDTH, super.getMinimumSize().height);
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

    public List<Card> getCards() { return cards; }
    public boolean isPlayer1Owner() { return isPlayer1Owner; }
}