package it.unibo.burraco.model.cards;

/**
 * Represents a generic playing card in Burraco.
 * Provides access to the card's suit, face value, numerical rank,
 * and wildcard status.
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
