package card;

import java.util.*;

/**
     * Constructs a new deck.
     *  - Two standard 52-card sets
     *  - Two Jokers for each set (4 Jokers total)
     * After creation, the deck is shuffled randomly.
     */
public class DeckImpl implements Deck {

    private List<Card> cards;

    public DeckImpl() {

        /* Internal list that stores all the cards in the deck */
        cards = new ArrayList<>();

        String[] seeds = {"♠", "♥", "♣", "♦"};
        String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        for (int j=0; j<2; j++) {
            for (String seed : seeds) {
                for (String value : values) {
                    cards.add(new CardImpl(seed, value));
                }
            }
            cards.add(new CardImpl("", "Jolly"));
            cards.add(new CardImpl("", "Jolly"));
        }
        Collections.shuffle(cards);
    }

    /**
     * Draws and removes the first card from the deck.
     *
     * @return the drawn card if the deck is not empty, otherwise null.
     */
    @Override
    public Card draw() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }


    /**
     * Checks whether the deck is empty.
     *
     * @return true if the deck contains no cards, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Returns the current list of cards in the deck.
     *
     * Note: This method exposes the internal list directly.
     * External modifications will affect the deck.
     *
     * @return the list of cards.
     */
    @Override
    public List<Card> getCards() {
        return cards;
    }


}
