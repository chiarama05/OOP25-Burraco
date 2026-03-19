package it.unibo.burraco.core.distributioncard;

import java.util.List;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.*;

/**
 * Handles the initial distribution of cards in the Burraco game.
 */
public interface DistributionManager {

    /**
     * Distributes initial cards to both players and prepares the discard pile.
     */
    void distributeInitialCards(Player player1, Player player2, Deck deck, it.unibo.burraco.model.discard.DiscardPile modelDiscardPile);

    /**
     * Returns the initial discard pile after distribution.
     */
    List<Card> getInitialDiscardPile();
}
