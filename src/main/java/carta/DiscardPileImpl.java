package carta;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the DiscardPile interface using an ArrayList.
 * Represents a pile of discarded cards in a card game.
 */

public class DiscardPileImpl implements DiscardPile{

    // List to store the cards in the discard pile
    private List<Card> cards = new ArrayList<>();

    /**
     * Adds a single card to the discard pile.
     */
    @Override
    public void add(Card card){
        this.cards.add(card);
    }

    /**
     * Adds multiple cards to the discard pile.
     */
    @Override
    public void addAll(List<Card> cards){
        this.cards.addAll(cards);
    }

    /**
     * Draws (removes and returns) the last card added to the discard pile.
     */
    @Override
    public Card drawLast(){
        if(!cards.isEmpty()){
            return cards.remove(cards.size()-1);
        }
        return null;
    }

    /**
     * Returns the list of cards currently in the discard pile.
     */
    @Override
    public List<Card> getCards(){
        return cards;
    }

    /**
     * Checks if the discard pile is empty.
     */
    @Override
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     * Clears all cards from the discard pile.
     */
    @Override
    public void clear() {
        cards.clear();
    }

}
