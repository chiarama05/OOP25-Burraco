package it.unibo.burraco.controller.distribution;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Interface defining the contract for the initial card distribution.
 * It manages the setup of players' hands, side pots, and the starting discard pile.
 */
public interface DistributionManager {

    /**
     * Executes the initial setup of the match.
     * Draws cards from the deck to fill players' hands, create side pots,
     * and initialize the discard pile.
     *
     * @param player1 the first player.
     * @param player2 the second player.
     * @param deck the game deck to draw from.
     * @param modelDiscardPile the discard pile model to be initialized.
     */
    void distributeInitialCards(Player player1,
                                Player player2,
                                Deck deck,
                                DiscardPile modelDiscardPile);

}
