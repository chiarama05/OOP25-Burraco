package it.unibo.burraco.view.distribution;

import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.discard.DiscardView;
import it.unibo.burraco.view.hand.HandViewImpl;

import javax.swing.*;

public class InitialDistributionView {

    private final HandViewImpl handPlayer1;
    private final HandViewImpl handPlayer2;

    public InitialDistributionView(JPanel discardPanel, SelectionCardManager selectionManager) {
        this.handPlayer1 = new HandViewImpl(selectionManager);
        this.handPlayer2 = new HandViewImpl(selectionManager);
    }

    public HandViewImpl getPlayer1HandView() {
        return handPlayer1;
    }

    public HandViewImpl getPlayer2HandView() {
        return handPlayer2;
    }

    public void refresh(Player p1, Player p2, DiscardView discardView, DiscardPile discardPile) {
        handPlayer1.refreshHand(p1.getHand());
        handPlayer2.refreshHand(p2.getHand());
        discardView.updateDiscardPile(discardPile.getCards());
    }
}