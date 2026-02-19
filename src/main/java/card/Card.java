package card;

/**
 * The Card interface represents a generic playing card.
 * It defines the essential properties that every card
 * must provide: seed, value and numerical value.
 */

public interface Card {

    /**
     * Returns the seed of the card.
     */
    String getSeed();   

    /**
     * Returns the value of the card.
     */
    String getValue();

    /**
     * Returns the numerical value of the card.
     */
    int getNumericalValue();
}
