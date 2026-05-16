package it.unibo.burraco.model.cards;

/**
 * Enumerates every suit (seed) a card can belong to in Burraco.
 *
 * <p>Replaces the raw Unicode strings ({@code "♠"}, {@code "♥"}, etc.)
 * that were previously compared with {@link String#contains} in view classes
 * to decide foreground colour.
 *
 * <p>{@link #isRed()} centralises the red-suit check so the view never
 * needs to inspect symbol strings again.
 */
public enum Seed {

    SPADES  ("♠"),
    HEARTS  ("♥"),
    CLUBS   ("♣"),
    DIAMONDS("♦"),
    JOKER   ("♕");   // used exclusively by Jolly cards

    private final String symbol;

    Seed(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * The Unicode symbol for this suit (e.g. {@code "♠"}, {@code "♥"}).
     *
     * @return the suit symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Hearts and Diamonds are conventionally displayed in red.
     *
     * @return {@code true} if this suit should be rendered in red
     */
    public boolean isRed() {
        return this == HEARTS || this == DIAMONDS;
    }

    /**
     * Returns the suit symbol, so that {@code card.getSeed().toString()}
     * produces the same output as the old {@code card.getSeed()} string field.
     *
     * @return the suit symbol
     */
    @Override
    public String toString() {
        return symbol;
    }
}