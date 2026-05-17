package it.unibo.burraco.model.cards;

/**
 * Enumerates every possible face value a card can have in Burraco.
 * Each constant carries its display string and its numerical rank.
 */
public enum CardValue {

    ACE("A", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    JOLLY("Jolly", 0);

    private final String display;
    private final int numericalValue;

    CardValue(final String display, final int numericalValue) {
        this.display = display;
        this.numericalValue = numericalValue;
    }

    /**
     * Returns the string displayed on the card face, for example "A", "10" or "Jolly".
     *
     * @return the display string for this value
     */
    public String getDisplay() {
        return display;
    }

    /**
     * The numeric rank of this card (1 for Ace, 13 for King, 0 for Jolly).
     *
     * @return the numerical value
     */
    public int getNumericalValue() {
        return numericalValue;
    }

    /**
     * Checks whether this value represents a Jolly card.
     *
     * @return true if this value is JOLLY
     */
    public boolean isJolly() {
        return this == JOLLY;
    }

    /**
     * Checks whether this card can potentially act as a wildcard.
     * A card is a potential wildcard if it is a Jolly or a Two.
     * Whether a Two actually acts as a wildcard depends on context.
     *
     * @return true if this value is JOLLY or TWO
     */
    public boolean isPotentialWildcard() {
        return this == JOLLY || this == TWO;
    }

    /**
     * Returns the display string of this value.
     *
     * @return the display string for this value
     */
    @Override
    public String toString() {
        return display;
    }
}
