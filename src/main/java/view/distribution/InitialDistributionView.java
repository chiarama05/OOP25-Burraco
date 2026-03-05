package view.distribution;

import javax.swing.*;
import java.awt.*;
import model.card.*;
import model.deck.DeckImpl;
import model.player.Player;
import core.distributioncard.DistributionManagerImpl;
import core.selectioncard.SelectionCardManager;
import view.hand.handImpl;

public class InitialDistributionView {

    private final handImpl handPlayer1;
    private final handImpl handPlayer2;
    private final JPanel discardPanel;

    public InitialDistributionView(JPanel discardPanel, SelectionCardManager selectionManager) {
    this.discardPanel = discardPanel;
    this.handPlayer1 = new handImpl(selectionManager);
    this.handPlayer2 = new handImpl(selectionManager);
    }

    public handImpl getPlayer1HandView() { return handPlayer1; }
    public handImpl getPlayer2HandView() { return handPlayer2; }

    public void distribute(Player player1, Player player2, DistributionManagerImpl distManager) {
        distManager.distributeInitialCards(player1, player2, new DeckImpl());

        // Mostra solo il contenuto interno delle singole mani
        handPlayer1.refreshHand(player1.getHand());
        handPlayer2.refreshHand(player2.getHand());

        // Scarti
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