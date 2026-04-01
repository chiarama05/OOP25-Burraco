package it.unibo.burraco.controller.distributioncard;

import java.util.List;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
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

    /**
     * Retrieves the state of the initial discard pile after distribution.
     *
     * @return a list of cards representing the initial discard pile.
     */
    List<Card> getInitialDiscardPile();
}
