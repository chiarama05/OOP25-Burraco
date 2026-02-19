package card;

import java.util.*;

/**
     * Constructs a new deck.
     * The deck is composed of two standard sets of cards (52 cards each)
     * plus two Jokers for each set.
     * After creation, the deck is shuffled randomly.
     */
public class DeckImpl implements Deck {

    private List<Card> cards;

    public DeckImpl() {
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
     */
    @Override
    public Card pick() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }


    /**
     * Checks whether the deck is empty.
     */
    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }


    /**
     * Returns the list of cards currently in the deck.
     */
    @Override
    public List<Card> getCards() {
        return cards;
    }


}
