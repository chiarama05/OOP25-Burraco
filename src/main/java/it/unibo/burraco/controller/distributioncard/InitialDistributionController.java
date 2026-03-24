package it.unibo.burraco.controller.distributioncard;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

public class InitialDistributionController {

    private final DistributionManagerImpl distManager;

    public InitialDistributionController(DistributionManagerImpl distManager) {
        this.distManager = distManager;
    }

    public void distribute(Player p1, Player p2, Deck deck, DiscardPile discardPile) {
        distManager.distributeInitialCards(p1, p2, deck, discardPile);
    }
}