package it.unibo.burraco.model.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import it.unibo.burraco.model.player.Player;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import it.unibo.burraco.model.cards.CardImpl;
import it.unibo.burraco.model.cards.CardValue;
import it.unibo.burraco.model.cards.Seed;

class ClosureValidatorTest {
    @Test
    void testPlayerWithCardsIsOk() {
        final ClosureValidator validator = new ClosureValidator();
        final Player p = mock(Player.class);
        when(p.getHand()).thenReturn(List.of(new CardImpl(Seed.HEARTS, CardValue.ACE)));
        
        assertEquals(ClosureState.OK, validator.evaluate(p));
    }
}
