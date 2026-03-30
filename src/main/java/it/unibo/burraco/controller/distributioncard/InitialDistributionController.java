package it.unibo.burraco.controller.distributioncard;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Controller responsible for orchestrating the initial card distribution phase.
 * It now relies on the {@link DistributionManager} interface, following the 
 * Dependency Inversion Principle for better decoupling and testability.
 */
public class InitialDistributionController {

    private final DistributionManager distManager;

    /**
     * Constructs the controller by injecting a distribution manager implementation.
     * 
     * @param distManager any implementation of the DistributionManager interface.
     */
    public InitialDistributionController(final DistributionManager distManager) {
        this.distManager = distManager;
    }

    /**
     * Initiates the distribution process by delegating to the manager.
     * It coordinates the transfer of cards from the deck to players and the discard pile.
     * 
     * @param p1 the first player.
     * @param p2 the second player.
     * @param deck the game deck.
     * @param discardPile the initial discard pile to be populated.
     */
    public void distribute(final Player p1, 
                           final Player p2, 
                           final Deck deck, 
                           final DiscardPile discardPile) {
        distManager.distributeInitialCards(p1, p2, deck, discardPile);
    }
}