package it.unibo.burraco.model.cards;

/**
 * The Card interface represents a generic playing card.
 * It defines the essential properties that every card must provide:
 * seed, value and numerical value.
 */
public interface Card {

    /**
     * Returns the seed of the card.
     *
     * @return the seed of the card
     */
    Seed getSeed();

    /**
     * Returns the value of the card (e.g. "A", "10", "K").
     *
     * @return the face value of the card
     */
    CardValue getValue();

    /**
     * Returns the numerical representation of the card's value.
     *
     * @return the numerical value of the card
     */
    int getNumericalValue();

    /**
     * Checks if the card is currently being used as a wildcard.
     *
     * @return true if the card acts as a wildcard, false otherwise
     */
    boolean isUsedAsWildcard();
}
