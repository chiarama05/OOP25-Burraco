package view.distribution;

import model.card.Card;
import model.deck.Deck;
import model.player.PlayerImpl;
import core.distributioncard.DistributionManagerImpl;
import core.selectioncard.SelectionCardManager;
import view.hand.handImpl;

import javax.swing.*;
import java.awt.*;

public class InitialDistributionView {

    private final handImpl handPlayer1;
    private final handImpl handPlayer2;
    private final JPanel discardPanel;

    public InitialDistributionView(JPanel discardPanel, SelectionCardManager selectionManager) {
    this.discardPanel = discardPanel;
    this.handPlayer1 = new handImpl(selectionManager);
    this.handPlayer2 = new handImpl(selectionManager);
    }

    public handImpl getPlayer1HandView() {
        return handPlayer1; 
    }

    public handImpl getPlayer2HandView() {
        return handPlayer2; 
    }

    public void distribute(PlayerImpl player1, PlayerImpl player2, DistributionManagerImpl distManager, Deck deck) {
        distManager.distributeInitialCards(player1, player2, deck);

        handPlayer1.refreshHand(player1.getHand());
        handPlayer2.refreshHand(player2.getHand());

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