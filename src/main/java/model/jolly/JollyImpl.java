package model.jolly;

import java.util.List;
import java.util.stream.Collectors;

import model.card.Card;

/**
 * Implementation of the Jolly interface.
 * Handles the behavior of wildcards (Jolly) and "2" cards in card games.
 */
public class JollyImpl implements Jolly {

    /** The card associated with this Jolly implementation */
    private final Card card;

     /**
     * Constructor that initializes the Jolly with a specific card.
     * 
     * @param card the card to wrap as a Jolly
     */
    public JollyImpl(Card card) {
        this.card = card;
    }
    /**
     * Determines if this card is a pure Jolly (always acts as a wildcard).
     *
     * @return true if the card is a Jolly, false otherwise
     */
    @Override
    public boolean isPureJolly() {
        return card.getValue().equals("Jolly");
    }


     /**
     * Determines if this card acts as a Jolly (wildcard) in a given context.
     * For example, a "2" may act as a wildcard unless it is a natural two in a straight (A-2-3).
     *
     * @param context the list of cards representing the current set or straight
     * @return true if the card acts as a Jolly in this context, false otherwise
     */
    @Override
    public boolean isJolly(List<Card> context) {
        if (isPureJolly()) return true;
        if (!card.getValue().equals("2")) return false;

        /* 2 → acts as a wildcard in straights IF NOT natural */ 
        return !isNaturalTwo(card, context);
    }


    /**
     * Determines if a "2" card is natural in a straight (A-2-3).
     * A natural two is not considered a wildcard.
     *
     * @param two the 2 card to check
     * @param straight the list of cards forming the straight
     * @return true if the 2 is natural, false otherwise
     */
    private boolean isNaturalTwo(Card two, List<Card> straight) {
        if (!two.getValue().equals("2")) return false;

        List<Card> realCards = straight.stream()
                .filter(c -> !c.getValue().equals("2") && !c.getValue().equals("Jolly"))
                .collect(Collectors.toList());

        if (realCards.isEmpty()) return false;

        /* Natural two must have the same suit as the real cards */
        String suit = realCards.get(0).getSeed();
        if (!two.getSeed().equals(suit)) return false;

        /* Check for presence of Ace and 3 to form a natural sequence (A-2-3) */
        boolean hasAce = realCards.stream().anyMatch(c -> c.getValue().equals("A"));
        boolean hasThree = realCards.stream().anyMatch(c -> c.getValue().equals("3"));

        return hasAce && hasThree;
    }


    /**
     * Returns the underlying card associated with this Jolly implementation.
     *
     * @return the wrapped Card object
    */
    public Card getCard() {
        return card;
    }

}
