package core.distributioncard;

import java.util.List;

import model.deck.Deck;
import player.Player;
import model.card.*;

/**
 * Handles the initial distribution of cards in the Burraco game.
 */
public interface DistributionManager {

    /**
     * Distributes initial cards to both players and prepares the discard pile.
     */
    void distributeInitialCards(Player player1, Player player2, Deck deck);

    /**
     * Returns the initial discard pile after distribution.
     */
    List<Card> getInitialDiscardPile();
}
