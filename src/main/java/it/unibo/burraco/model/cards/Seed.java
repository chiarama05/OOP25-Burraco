package it.unibo.burraco.model.cards;

/**
 * Enumerates every suit a card can belong to in Burraco.
 * The JOKER suit is used exclusively by Jolly cards.
 */
public enum Seed {

    SPADES("♠"),
    HEARTS("♥"),
    CLUBS("♣"),
    DIAMONDS("♦"),
    JOKER("♕");

    private final String symbol;

    Seed(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the Unicode symbol for this suit, for example "♠" or "♥".
     *
     * @return the suit symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Checks whether this suit should be rendered in red.
     * Hearts and Diamonds are conventionally displayed in red.
     *
     * @return true if this suit is Hearts or Diamonds
     */
    public boolean isRed() {
        return this == HEARTS || this == DIAMONDS;
    }

    /**
     * Returns the suit symbol as a string.
     *
     * @return the suit symbol
     */
    @Override
    public String toString() {
        return symbol;
    }
}
