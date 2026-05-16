package it.unibo.burraco.model.cards;

/**
 * Enumerates every possible face value a card can have in Burraco.
 *
 * <p>Each constant carries its display string and its numerical rank,
 * replacing all raw {@code String} comparisons that were scattered
 * across the codebase (e.g. {@code "Jolly".equalsIgnoreCase(...)},
 * {@code "2".equals(...)}).
 *
 * <p>Helper predicates ({@link #isJolly()}, {@link #isPotentialWildcard()})
 * centralise wildcard logic so no other class needs to repeat it.
 */
public enum CardValue {

    ACE  ("A",     1),
    TWO  ("2",     2),
    THREE("3",     3),
    FOUR ("4",     4),
    FIVE ("5",     5),
    SIX  ("6",     6),
    SEVEN("7",     7),
    EIGHT("8",     8),
    NINE ("9",     9),
    TEN  ("10",   10),
    JACK ("J",    11),
    QUEEN("Q",    12),
    KING ("K",    13),
    JOLLY("Jolly", 0);

    private final String display;
    private final int numericalValue;

    CardValue(final String display, final int numericalValue) {
        this.display = display;
        this.numericalValue = numericalValue;
    }

    /**
     * The string displayed on the card face (e.g. {@code "A"}, {@code "10"}, {@code "Jolly"}).
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
     * @return {@code true} if this value is {@link #JOLLY}
     */
    public boolean isJolly() {
        return this == JOLLY;
    }

    /**
     * A card is a potential wildcard in Burraco if it is a Jolly or a Two.
     * Whether a Two actually acts as a wildcard depends on context
     * (see {@link it.unibo.burraco.model.rules.CombinationValidator}).
     *
     * @return {@code true} if this value is {@link #JOLLY} or {@link #TWO}
     */
    public boolean isPotentialWildcard() {
        return this == JOLLY || this == TWO;
    }

    /**
     * Returns the display string, so that {@code card.getValue().toString()}
     * produces the same output as the old {@code card.getValue()} string field.
     *
     * @return the display string for this value
     */
    @Override
    public String toString() {
        return display;
    }
}