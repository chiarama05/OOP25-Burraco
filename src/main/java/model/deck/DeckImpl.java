package model.deck;

import java.util.*;
import model.card.Card;
import model.card.CardImpl;

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
