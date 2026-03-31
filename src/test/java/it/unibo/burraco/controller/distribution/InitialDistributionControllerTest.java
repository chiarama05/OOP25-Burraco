package it.unibo.burraco.controller.distribution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;

class InitialDistributionControllerTest {

    @Test
    void testDistributeDelegation() {
        final DistributionManagerImpl mockManager = mock(DistributionManagerImpl.class);
        final InitialDistributionController controller = new InitialDistributionController(mockManager);
        final Player p1 = mock(Player.class);
        final Player p2 = mock(Player.class);
        final Deck deck = mock(Deck.class);
        final DiscardPile discard = mock(DiscardPile.class);

        controller.distribute(p1, p2, deck, discard);
        
        verify(mockManager).distributeInitialCards(p1, p2, deck, discard);
    }
}
