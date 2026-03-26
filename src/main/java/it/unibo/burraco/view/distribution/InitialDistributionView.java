package it.unibo.burraco.view.distribution;

import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.discard.DiscardView;
import it.unibo.burraco.view.hand.HandViewImpl;

import javax.swing.*;
import java.util.List;

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

    public void refresh(List<Card> hand1, List<Card> hand2,
                        DiscardView discardView, List<Card> discardPile) {
        handPlayer1.refreshHand(hand1);
        handPlayer2.refreshHand(hand2);
        discardView.updateDiscardPile(discardPile);
    }
}