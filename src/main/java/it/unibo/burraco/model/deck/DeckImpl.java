package it.unibo.burraco.model.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;

/**
 * Constructs a new deck.
 *  - Two standard 52-card sets
 *  - Two Jokers for each set (4 Jokers total)
 * After creation, the deck is shuffled randomly.
 */
public final class DeckImpl implements Deck {

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
        final String[] seeds = {"♠", "♥", "♣", "♦"};
        final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (int j = 0; j < 2; j++) {
            for (final String seed : seeds) {
                for (final String value : values) {
                    cards.add(new CardImpl(seed, value));
                }
            }
            cards.add(new CardImpl("♕", "Jolly"));
            cards.add(new CardImpl("♕", "Jolly"));
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
