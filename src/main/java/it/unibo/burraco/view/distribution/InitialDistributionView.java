package it.unibo.burraco.view.distribution;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.core.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.core.selectioncard.SelectionCardManager;
import it.unibo.burraco.view.hand.handImpl;

import javax.swing.*;

public class InitialDistributionView {

    private final handImpl handPlayer1;
    private final handImpl handPlayer2;

    public InitialDistributionView(JPanel discardPanel, SelectionCardManager selectionManager) {
    this.handPlayer1 = new handImpl(selectionManager);
    this.handPlayer2 = new handImpl(selectionManager);
    }

    public handImpl getPlayer1HandView() {
        return handPlayer1; 
    }

    public handImpl getPlayer2HandView() {
        return handPlayer2; 
    }

    public void distribute(PlayerImpl player1, PlayerImpl player2, DistributionManagerImpl distManager, Deck deck, 
                           it.unibo.burraco.view.discard.DiscardView discardView, it.unibo.burraco.model.discard.DiscardPile modelDiscardPile) {

        distManager.distributeInitialCards(player1, player2, deck, modelDiscardPile);

        handPlayer1.refreshHand(player1.getHand());
        handPlayer2.refreshHand(player2.getHand());

        discardView.updateDiscardPile(modelDiscardPile.getCards());
    }
}