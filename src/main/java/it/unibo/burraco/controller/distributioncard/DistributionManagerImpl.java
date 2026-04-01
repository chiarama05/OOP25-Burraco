package it.unibo.burraco.controller.distributioncard;

import java.util.ArrayList;
import java.util.List;
import it.unibo.burraco.model.card.*;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Concrete implementation of the {@link DistributionManager}.
 * Handles the sequential drawing of cards to ensure a fair and rule-compliant
 * start of a Burraco round.
 */
public final class DistributionManagerImpl implements DistributionManager{

    private static final int INITIAL_HAND_SIZE = 11;
    private final List<Card> discardPile = new ArrayList<>();

    @Override
    public void distributeInitialCards(final Player player1,
                                final Player player2,
                                final Deck deck,
                                final DiscardPile modelDiscardPile){

        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            player1.addCardHand(deck.draw());
            player2.addCardHand(deck.draw());
        }

        final List<Card> pot1 = new ArrayList<>();
        final List<Card> pot2 = new ArrayList<>();

        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
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
