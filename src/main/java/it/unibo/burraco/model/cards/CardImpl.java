package it.unibo.burraco.model.cards;

/**
 * Implementation of the {@link Card} interface.
 * Represents a concrete playing card with a seed and a value.
 */
public final class CardImpl implements Card {

    private final Seed seed;
    private final CardValue value;
    private boolean wildcard;

    /**
     * Constructs a CardImpl with the specified seed and value.
     *
     * @param seed the seed of the card
     * @param value the face value of the card
     */
    public CardImpl(final Seed seed, final CardValue value) {
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
    public Seed getSeed() {
        return this.seed;
    }

    @Override
    public CardValue getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value.getDisplay() + seed.getSymbol();
    }

    @Override
    public int getNumericalValue() {
        return this.value.getNumericalValue();
    }
}
