package it.unibo.burraco.core.distributioncard;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.PlayerImpl;

public class InitialDistributionController {

    private final DistributionManagerImpl distManager;

    public InitialDistributionController(DistributionManagerImpl distManager) {
        this.distManager = distManager;
    }

    public void distribute(PlayerImpl player1, PlayerImpl player2, Deck deck, DiscardPile discardPile) {
        distManager.distributeInitialCards(player1, player2, deck, discardPile);
    }
}