package it.unibo.burraco.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.score.CardPoint;

public class CardPointTest {

    @Test void jollyIs30()  { 
        assertEquals(30, CardPoint.getCardPoints(new CardImpl("Jolly", "Jolly"))); 
    }

    @Test void twoIs20()    { 
        assertEquals(20, CardPoint.getCardPoints(new CardImpl("♥", "2"))); 
    }

    @Test void aceIs15()    { 
        assertEquals(15, CardPoint.getCardPoints(new CardImpl("♥", "A"))); 
    }

    @Test void kingIs10()   { 
        assertEquals(10, CardPoint.getCardPoints(new CardImpl("♥", "K"))); 
    }

    @Test void queenIs10()  { 
        assertEquals(10, CardPoint.getCardPoints(new CardImpl("♥", "Q"))); 
    }

    @Test void jackIs10()   { 
        assertEquals(10, CardPoint.getCardPoints(new CardImpl("♥", "J"))); 
    }

    @Test void tenIs10()    { 
        assertEquals(10, CardPoint.getCardPoints(new CardImpl("♥", "10"))); 
    }

    @Test void nineIs10()   { 
        assertEquals(10, CardPoint.getCardPoints(new CardImpl("♥", "9"))); 
    }

    @Test void eightIs10()  { 
        assertEquals(10, CardPoint.getCardPoints(new CardImpl("♥", "8"))); 
    }

    @Test void sevenIs5()   { 
        assertEquals(5,  CardPoint.getCardPoints(new CardImpl("♥", "7"))); 
    }

    @Test void sixIs5()     { 
        assertEquals(5,  CardPoint.getCardPoints(new CardImpl("♥", "6"))); 
    }

    @Test void fiveIs5()    { 
        assertEquals(5,  CardPoint.getCardPoints(new CardImpl("♥", "5"))); 
    }

    @Test void fourIs5()    { 
        assertEquals(5,  CardPoint.getCardPoints(new CardImpl("♥", "4"))); 
    }

    @Test void threeIs5()   { 
        assertEquals(5,  CardPoint.getCardPoints(new CardImpl("♥", "3"))); 
    }

    @Test void unknownIs0() { 
        assertEquals(0,  CardPoint.getCardPoints(new CardImpl("♥", "InvalidValue"))); 
    }
}
