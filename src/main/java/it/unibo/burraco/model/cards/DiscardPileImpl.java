package it.unibo.burraco.model.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the {@link DiscardPile} interface using an {@link ArrayList}.
 * Cards are managed in a LIFO (Last-In-First-Out) manner for draw operations.
 */
public final class DiscardPileImpl implements DiscardPile {

    /**
     * Internal list used to store the discarded cards.
     */
    private final List<Card> cards = new ArrayList<>();

    @Override
    public void add(final Card card) {
        this.cards.add(card);
    }

    @Override
    public void addAll(final List<Card> cardsToAdd) {
        this.cards.addAll(cardsToAdd);
    }

    @Override
    public Card drawLast() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1);
        }
        return null;
    }

    @Override
    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public void reset() {
        this.cards.clear();
    }
}
