package it.unibo.burraco.model.card;

import it.unibo.burraco.model.score.CardPoint;

/**
 * Implementation of the {@link Card} interface.
 * Represents a concrete playing card with a seed and a value.
 */
public final class CardImpl implements Card {

    private final String seed;
    private final String value;
    private boolean wildcard;

    /**
     * Constructs a CardImpl with the specified seed and value.
     * 
     * @param seed the seed of the card
     * @param value the face value of the card
     */
    public CardImpl(final String seed, final String value) {
        this.seed = seed;
        this.value = value;
    }

    /**
     * Sets the wildcard status of the card.
     * 
     * @param status true to set the card as a wildcard, false otherwise
     */
    public void setAsWildcard(final boolean status) {
        this.wildcard = status;
    }

    @Override
    public boolean isUsedAsWildcard() {
        return this.wildcard;
    }

    @Override
    public String getSeed() {
        return this.seed;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of the card.
     * 
     * @return a string combining value and seed (e.g. "A♠")
     */
    @Override
    public String toString() {
        return value + seed; 
    }

    /**
     * Returns the numerical value associated with the card value.
     * Face cards are mapped as follows:
     * A = 1, J = 11, Q = 12, K = 13.
     * "Jolly" is mapped to 0.
     * "2" is also treated as a wildcard/jolly in some contexts.
     * Returns -1 if the value is not recognized.
     * 
     * @return the numerical value of the card
     */
    @Override
    public int getNumericalValue() {
        return switch (this.value) {
            case "A" -> CardPoint.RANK_ACE;
            case "2" -> CardPoint.RANK_TWO;
            case "3" -> CardPoint.RANK_THREE;
            case "4" -> CardPoint.RANK_FOUR;
            case "5" -> CardPoint.RANK_FIVE;
            case "6" -> CardPoint.RANK_SIX;
            case "7" -> CardPoint.RANK_SEVEN;
            case "8" -> CardPoint.RANK_EIGHT;
            case "9" -> CardPoint.RANK_NINE;
            case "10" -> CardPoint.RANK_TEN;
            case "J" -> CardPoint.RANK_JACK;
            case "Q" -> CardPoint.RANK_QUEEN;
            case "K" -> CardPoint.RANK_KING;
            case "Jolly" -> 0;
            default -> -1;
        };
    }
}
