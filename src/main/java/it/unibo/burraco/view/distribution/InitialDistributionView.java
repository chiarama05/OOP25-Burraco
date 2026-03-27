package it.unibo.burraco.view.distribution;

import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.discardcard.discard.DiscardView;
import it.unibo.burraco.view.hand.HandView;
import it.unibo.burraco.view.hand.HandViewImpl;

import javax.swing.*;
import java.util.List;

/**
 * Orchestrates the initial visual setup of the game table.
 * It manages the transition from the distribution logic to the UI by 
 * synchronizing both players' hands and the discard pile.
 */
public class InitialDistributionView {

    private final HandView handPlayer1;
    private final HandView handPlayer2;

    /**
     * Initializes the view components for player hands.
     * @param discardPanel context panel for the discard pile.
     * @param selectionManager the manager shared between hands to handle card selection.
     */
    public InitialDistributionView(JPanel discardPanel, SelectionCardManager selectionManager) {
        this.handPlayer1 = new HandViewImpl(selectionManager);
        this.handPlayer2 = new HandViewImpl(selectionManager);
    }

    /** 
     * @return the HandView interface for Player 1. 
     */
    public HandView getPlayer1HandView() {
        return handPlayer1;
    }

    /** 
     * @return the HandView interface for Player 2. 
     */
    public HandView getPlayer2HandView() {
        return handPlayer2;
    }

    /**
     * Updates all relevant UI components after the initial cards have been dealt.
     * This method ensures an atomic visual update for both players and the table.
     * @param hand1 cards to be displayed in Player 1's hand.
     * @param hand2 cards to be displayed in Player 2's hand.
     * @param discardView the component handling the discard pile visualization.
     * @param discardPile the initial list of cards for the discard pile.
     */
    public void refresh(List<Card> hand1, List<Card> hand2,
                        DiscardView discardView, List<Card> discardPile) {
        // Delegating refresh logic to the respective hand components
        handPlayer1.refreshHand(hand1);
        handPlayer2.refreshHand(hand2);

        // Updating the discard pile view
        discardView.updateDiscardPile(discardPile);
    }
}