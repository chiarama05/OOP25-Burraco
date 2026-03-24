package it.unibo.burraco.view.distribution;

import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.view.discard.DiscardView;
import it.unibo.burraco.view.hand.HandImpl;

import javax.swing.*;

public class InitialDistributionView {

    private final HandImpl handPlayer1;
    private final HandImpl handPlayer2;

    public InitialDistributionView(JPanel discardPanel, SelectionCardManager selectionManager) {
        this.handPlayer1 = new HandImpl(selectionManager);
        this.handPlayer2 = new HandImpl(selectionManager);
    }

    public HandImpl getPlayer1HandView() {
        return handPlayer1;
    }

    public HandImpl getPlayer2HandView() {
        return handPlayer2;
    }

    public void refresh(PlayerImpl player1, PlayerImpl player2,
                        DiscardView discardView, DiscardPile modelDiscardPile) {
        handPlayer1.refreshHand(player1.getHand());
        handPlayer2.refreshHand(player2.getHand());
        discardView.updateDiscardPile(modelDiscardPile.getCards());
    }
}