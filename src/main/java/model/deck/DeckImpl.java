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
        this.cards = new ArrayList<>();
        initializeDeck();
    }

    /**
     * Deck's reset
     */
    public void reset() {
        this.cards.clear();
        initializeDeck();
    }

    private void initializeDeck() {
        String[] seeds = {"♠", "♥", "♣", "♦"};
        String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        //Two decks with 54 cards (52 + 2 Jolly) = 108 cards
        for (int j = 0; j < 2; j++) {
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
