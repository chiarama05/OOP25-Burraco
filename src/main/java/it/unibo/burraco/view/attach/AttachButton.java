package it.unibo.burraco.view.attach;

import it.unibo.burraco.controller.combination.straight.StraightUtils;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.burraco.BurracoStyleManager;
import it.unibo.burraco.view.table.TableView;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
public final class AttachButton extends JButton implements AttachView {

    private static final long serialVersionUID = 1L;

    private static final int FIXED_WIDTH = 64;
    private static final int VERTICAL_STRUT_SIZE = 8;
    private static final int FONT_SIZE_JOLLY = 28;
    private static final int FONT_SIZE_NORMAL = 22;
    private static final int COLOR_JOLLY_R = 219;
    private static final int COLOR_JOLLY_G = 112;
    private static final int COLOR_JOLLY_B = 147;
    private static final int GAP = 5;
    private static final int BORDER_PADDING = 10;
    private static final int LINE_THICKNESS = 1;
    private static final String JOLLY_VALUE = "Jolly";
    private static final String TWO_VALUE = "2";

    private final List<Card> cards;
    private final TableView tableView;
    private final boolean isPlayer1Owner;
    private BiConsumer<List<Card>, AttachButton> onAttachAction;

    /**
     * Constructs the button, initializes its layout and triggers the first visual render.
     *
     * @param initialCards   the cards in this combination
     * @param tableView      the main table view
     * @param isPlayer1Owner true if this combination belongs to Player 1
     */
    public AttachButton(final List<Card> initialCards, final TableView tableView, final boolean isPlayer1Owner) {
        this.cards = initialCards;
        this.tableView = tableView;
        this.isPlayer1Owner = isPlayer1Owner;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, LINE_THICKNESS),
                BorderFactory.createEmptyBorder(BORDER_PADDING, GAP, BORDER_PADDING, GAP)));
        this.updateVisuals();
        this.addActionListener(e -> handleAttachAction());
    }

    /**
     * Registers the action to execute when this button is clicked.
     *
     * @param handler a consumer receiving the selected cards and this button
     */
    public void setOnAttachAction(final BiConsumer<List<Card>, AttachButton> handler) {
        this.onAttachAction = handler;
    }

    /**
     * Collects the currently selected cards from the hand view
     * and forwards them to the registered attach action handler.
     */
    private void handleAttachAction() {
        if (this.onAttachAction == null) return;
        final List<Card> selected = new ArrayList<>(
                this.tableView.getHandViewForCurrentPlayer(isPlayer1Owner).getSelectedCards());
        this.onAttachAction.accept(selected, this);
    }

    @Override
    public void showAttachError(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    
    @Override
    public void updateCombinationVisuals() {
        this.updateVisuals();
    }

    @Override
    public void onAttachSuccess(final Player p, final boolean isPlayer1) {
        this.tableView.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
        this.tableView.refreshHandPanel(isPlayer1, p.getHand());
    }

    @Override
    public void onAttachTakePot(final Player p, final boolean isPlayer1) {
        this.tableView.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
    }

    @Override
    public void onAttachClose(final Player p, final boolean isPlayer1) {
        this.tableView.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
        this.tableView.refreshHandPanel(isPlayer1, p.getHand());
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
            final List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(this.cards));
            Collections.reverse(ordered);
            this.cards.clear();
            this.cards.addAll(ordered);
        } else {
            final String baseValue = cards.stream()
                .filter(c -> !c.getValue().equalsIgnoreCase(JOLLY_VALUE))
                .map(Card::getValue)
                .findFirst()
                .orElse(null);

            final List<Card> naturals = new ArrayList<>();
            final List<Card> wildcards = new ArrayList<>();

            for (final Card c : this.cards) {
                final boolean isWild = c.getValue().equalsIgnoreCase(JOLLY_VALUE)
                || (c.getValue().equals(TWO_VALUE) && !TWO_VALUE.equals(baseValue));
                if (isWild) {
                    wildcards.add(c);
                } else {
                    naturals.add(c);
                }
            }

            naturals.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
            this.cards.clear();
            this.cards.addAll(wildcards); 
            this.cards.addAll(naturals);
        }

        this.setBorder(BorderFactory.createCompoundBorder(
                BurracoStyleManager.getBurracoBorder(this.cards),
                BorderFactory.createEmptyBorder(BORDER_PADDING, GAP, BORDER_PADDING, GAP)));
        this.setBackground(BurracoStyleManager.getBurracoBackground(this.cards));

        for (final Card c : this.cards) {
            this.renderCardLabel(c);
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
    private void renderCardLabel(final Card c) {
        final boolean isJolly = c.getValue().equalsIgnoreCase(JOLLY_VALUE);
        final JLabel label = new JLabel(isJolly ? c.getSeed() : c.toString());

        if (isJolly) {
            label.setFont(new Font("Segoe UI Symbol", Font.BOLD, FONT_SIZE_JOLLY));
            label.setForeground(new Color(COLOR_JOLLY_R, COLOR_JOLLY_G, COLOR_JOLLY_B));
        } else {
            label.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE_NORMAL));
            label.setForeground(
                    c.toString().contains("♥") || c.toString().contains("♦")
                            ? Color.RED : Color.BLACK);
        }

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SIZE));
    }

    /** @return the list of cards in this combination */
    public List<Card> getCards() { 
        return this.cards; 
    }

    /** @return true if this combination belongs to Player 1 */
    public boolean isPlayer1Owner() { 
        return this.isPlayer1Owner; 
    }
}
