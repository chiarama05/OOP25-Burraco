package model.jolly;

import java.util.List;
import java.util.stream.Collectors;

import core.combination.StraightUtils;
import model.card.Card;

/**
 * Implementation of the Jolly interface.
 * Handles the behavior of wildcards (Jolly) and "2" cards in card games.
 */
public class JollyImpl implements Jolly {

    // The card associated with this Jolly implementation 
    private final Card card;

     /**
     * Constructor that initializes the Jolly with a specific card.
     * 
     * @param card the card to wrap as a Jolly
     */
    public JollyImpl(Card card) {
        this.card = card;
    }
    
    
    @Override
    public boolean isPureJolly() {
        return card.getValue().equals("Jolly");
    }


    @Override
    public boolean isJolly(List<Card> context) {
        if (context == null || context.isEmpty()) {
            return false;
        }

        if (isPureJolly()) {
            return true;
        }
        if (!card.getValue().equals("2")) {
            return false;
        }

        return !StraightUtils.isNaturalTwo(card, context);
    }


    public Card getCard() {
        return card;
    }

}
