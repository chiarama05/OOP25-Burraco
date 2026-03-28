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

/**
 * Swing button representing an existing card combination on the table.
 * Displays the combination's cards vertically and handles attach actions
 * when the player clicks the button with selected cards.
 * Implements {@link AttachView} to react to attach outcomes from the controller.
 */
public class AttachButton extends JButton implements AttachView {

    private final List<Card> cards;
    private final TableView tableView;
    private final boolean isPlayer1Owner;
    private static final int FIXED_WIDTH = 64;
    private BiConsumer<List<Card>, AttachButton> onAttachAction;

    /**
     * Constructs the button, initializes its layout and triggers the first visual render.
     *
     * @param initialCards   the cards in this combination
     * @param tableView      the main table view
     * @param isPlayer1Owner true if this combination belongs to Player 1
     */
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

    /**
     * Registers the action to execute when this button is clicked.
     *
     * @param handler a consumer receiving the selected cards and this button
     */
    public void setOnAttachAction(BiConsumer<List<Card>, AttachButton> handler) {
        this.onAttachAction = handler;
    }

    /**
     * Collects the currently selected cards from the hand view
     * and forwards them to the registered attach action handler.
     */
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

    /**
     * Re-renders the combination cards, applying correct ordering:
     * - Straights are ordered by value and reversed for display
     * - Sets place wildcards (Jolly or 2 used as wildcard) at the top,
     *   followed by natural cards sorted by descending value
     */
    public void updateVisuals() {
        this.removeAll();

        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            Collections.reverse(ordered);
            cards.clear();
            cards.addAll(ordered);
        } else {
            String baseValue = cards.stream()
                .filter(c -> !c.getValue().equalsIgnoreCase("Jolly"))
                .map(Card::getValue)
                .findFirst()
                .orElse(null);

            List<Card> naturals = new ArrayList<>();
            List<Card> wildcards = new ArrayList<>();

            for (Card c : cards) {
                boolean isWild = c.getValue().equalsIgnoreCase("Jolly")
                || (c.getValue().equals("2") && !"2".equals(baseValue));
                if (isWild) wildcards.add(c);
                else naturals.add(c);
            }

            naturals.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
            cards.clear();
            cards.addAll(wildcards); 
            cards.addAll(naturals);
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

    /**
     * Renders a single card as a styled {@link JLabel} inside the button.
     * Jokers are displayed with a crown symbol in pink; other cards in red or black.
     *
     * @param c the card to render
     */
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

    /** @return the list of cards in this combination */
    public List<Card> getCards() { 
        return cards; 
    }

    /** @return true if this combination belongs to Player 1 */
    public boolean isPlayer1Owner() { 
        return isPlayer1Owner; 
    }
}