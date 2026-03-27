package it.unibo.burraco.controller.distributioncard;

import java.util.ArrayList;
import java.util.List;
import it.unibo.burraco.model.card.*;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.Player;

/**
 * Concrete implementation of the {@link DistributionManager}.
 * Handles the sequential drawing of cards to ensure a fair and rule-compliant 
 * start of a Burraco round.
 */
public class DistributionManagerImpl implements DistributionManager{

    // Internal storage for the initial discard pile state
    private List<Card> discardPile = new ArrayList<>();

    @Override
    public void distributeInitialCards(Player player1, Player player2, Deck deck, it.unibo.burraco.model.discard.DiscardPile modelDiscardPile){

        // Distribute 11 cards to each player's starting hand
        for (int i = 0; i < 11; i++) {
            player1.addCardHand(deck.draw());
            player2.addCardHand(deck.draw());
        }

        // Prepare the two "side pots" of 11 cards each
        List<Card> pot1 = new ArrayList<>();
        List<Card> pot2 = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            pot1.add(deck.draw());
            pot2.add(deck.draw());
        }

        // Assign the generated pots to the respective players
        player1.addToPot(pot1);
        player2.addToPot(pot2);

        // The discard pile starts with the first card from the remaining deck
        modelDiscardPile.add(deck.draw());
    }

    @Override
    public List<Card> getInitialDiscardPile() {
        // Returns a defensive copy of the internal discard pile list
        return new ArrayList<>(discardPile);
    }
}
