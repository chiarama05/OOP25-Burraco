package it.unibo.burraco.model.jolly;

import java.util.List;

import it.unibo.burraco.controller.combination.straight.StraightUtils;
import it.unibo.burraco.model.card.Card;

/**
 * Implementation of the Jolly interface.
 * Handles the behavior of wildcards (Jolly) and "2" cards in card games.
 */
public final class JollyImpl implements Jolly {

    private final Card card;

    /**
     * Constructor that initializes the Jolly with a specific card.
     *
     * @param card the card to wrap as a Jolly
     */
    public JollyImpl(final Card card) {
        this.card = card;
    }

    @Override
    public boolean isPureJolly() {
        return "Jolly".equals(card.getValue());
    }

    @Override
    public boolean isJolly(final List<Card> context) {
        if (context == null || context.isEmpty()) {
            return false;
        }
        return isPureJolly() 
            || "2".equals(card.getValue()) && !StraightUtils.isNaturalTwo(card, context);
    }

    /**
     * Returns the underlying card wrapped by this Jolly.
     *
     * @return the card
     */
    public Card getCard() {
        return card;
    }

}
