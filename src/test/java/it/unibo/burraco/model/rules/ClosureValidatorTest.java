package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.player.Player;
import static org.mockito.Mockito.*;

import java.util.List;

import it.unibo.burraco.model.cards.CardImpl;

class ClosureValidatorTest {
    @Test
    void testPlayerWithCardsIsOk() {
        final ClosureValidator validator = new ClosureValidator();
        final Player p = mock(Player.class);
        when(p.getHand()).thenReturn(List.of(new CardImpl("♥", "A")));
        
        assertEquals(ClosureState.OK, validator.evaluate(p));
    }
}
