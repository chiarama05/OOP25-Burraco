package it.unibo.burraco.controller.distribution;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;

class InitialDistributionControllerTest {

    @Test
    void testDistributeDelegation() {
        DistributionManagerImpl mockManager = mock(DistributionManagerImpl.class);
        InitialDistributionController controller = new InitialDistributionController(mockManager);
        
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Deck deck = mock(Deck.class);
        DiscardPile discard = mock(DiscardPile.class);

        controller.distribute(p1, p2, deck, discard);

        verify(mockManager).distributeInitialCards(p1, p2, deck, discard);
    }
}
