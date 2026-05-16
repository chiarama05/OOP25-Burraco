package it.unibo.burraco.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Standard Burraco deck implementation.
 * Uses {@link Seed} and {@link CardValue} enums instead of raw strings,
 * eliminating magic literals from card construction.
 */
public final class DeckImpl implements Deck {

    private static final Seed[] STANDARD_SEEDS = {
        Seed.SPADES, Seed.HEARTS, Seed.CLUBS, Seed.DIAMONDS
    };

    private static final CardValue[] STANDARD_VALUES = {
        CardValue.ACE,   CardValue.TWO,   CardValue.THREE, CardValue.FOUR,
        CardValue.FIVE,  CardValue.SIX,   CardValue.SEVEN, CardValue.EIGHT,
        CardValue.NINE,  CardValue.TEN,   CardValue.JACK,  CardValue.QUEEN,
        CardValue.KING
    };

    private static final int DECK_COPIES   = 2;
    private static final int JOLLY_PER_DECK = 2;

    private final List<Card> cards;

    /**
     * Constructs a new DeckImpl and initializes the full deck.
     */
    public DeckImpl() {
        this.cards = new ArrayList<>();
        initializeDeck();
    }

    /**
     * Clears the deck and regenerates it from scratch, including a new shuffle.
     */
    @Override
    public void reset() {
        this.cards.clear();
        initializeDeck();
    }

    /**
     * Builds the full deck: two sets of 52 cards plus 2 Jokers each, then shuffles.
     */
    private void initializeDeck() {
        for (int copy = 0; copy < DECK_COPIES; copy++) {
            for (final Seed seed : STANDARD_SEEDS) {
                for (final CardValue value : STANDARD_VALUES) {
                    cards.add(new CardImpl(seed, value));
                }
            }
            for (int j = 0; j < JOLLY_PER_DECK; j++) {
                cards.add(new CardImpl(Seed.JOKER, CardValue.JOLLY));
            }
        }
        Collections.shuffle(cards);
    }

    @Override
    public Card draw() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}