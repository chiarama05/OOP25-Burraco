package it.unibo.burraco.controller.distribution;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Controller that coordinates the initial card distribution phase.
 * It serves as an abstraction layer that delegates the distribution logic 
 * to an implementation of {@link DistributionManager}.
 */
public class InitialDistributionController {

    private final DistributionManager distManager;

    /**
     * Constructs a new InitialDistributionController.
     *
     * @param distManager the distribution logic provider to be used
     */
    public InitialDistributionController(final DistributionManager distManager) {
        this.distManager = distManager;
    }

    /**
     * Starts the distribution of cards to players, side pots, and the discard pile.
     * 
     * @param p1          the first player to be initialized
     * @param p2          the second player to be initialized
     * @param deck        the deck from which cards are drawn
     * @param discardPile the pile where the first card of the game is placed
     */
    public void distribute(final Player p1,
                           final Player p2,
                           final Deck deck,
                           final DiscardPile discardPile) {
        distManager.distributeInitialCards(p1, p2, deck, discardPile);
    }
}
