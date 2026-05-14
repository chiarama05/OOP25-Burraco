package it.unibo.burraco.controller.distribution;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Concrete implementation of {@link DistributionManager}.
 * It follows the standard Burraco rules: 11 cards to each player,
 * 11 cards for each side pot (pozzetto), and one starting card in the discard pile.
 */
public final class DistributionManagerImpl implements DistributionManager {

    private static final int INITIAL_HAND_SIZE = 11;

    @Override
    public void distributeInitialCards(final Player player1,
                                final Player player2,
                                final Deck deck,
                                final DiscardPile modelDiscardPile) {

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

        player1.addToPot(pot1);
        player2.addToPot(pot2);

        modelDiscardPile.add(deck.draw());
    }
}
