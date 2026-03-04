package view.distribution;

import javax.swing.*;
import java.awt.*;
import model.card.*;
import model.deck.DeckImpl;
import model.player.Player;
import core.distributioncard.DistributionManagerImpl;
import view.hand.handImpl;

public class InitialDistributionView {

    private final handImpl handPlayer1;
    private final handImpl handPlayer2;
    private final JPanel discardPanel;

    public InitialDistributionView(JPanel discardPanel) {
        this.discardPanel = discardPanel;
        this.handPlayer1 = new handImpl();
        this.handPlayer2 = new handImpl();
    }

    /**
     * Returns a panel containing both players' hands.
     */
    public JPanel getHandsPanel() {
        JPanel handsPanel = new JPanel(new GridLayout(2,1,10,10));
        handsPanel.add(handPlayer1);
        handsPanel.add(handPlayer2);
        return handsPanel;
    }

    /**
     * Distribute initial cards to both players and refresh GUI.
     */
    public void distribute(Player player1, Player player2, DistributionManagerImpl distManager) {
        // Use the concrete DeckImpl for distribution
        distManager.distributeInitialCards(player1, player2, new DeckImpl());

        // Refresh hands
        handPlayer1.refreshHand(player1.getHand());
        handPlayer2.refreshHand(player2.getHand());

        // Show initial discard pile
        discardPanel.removeAll();
        for (Card c : distManager.getInitialDiscardPile()) {
            JLabel lbl = new JLabel(c.toString());
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            lbl.setPreferredSize(new Dimension(60, 90));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            discardPanel.add(lbl);
        }
        discardPanel.revalidate();
        discardPanel.repaint();
    }
}
