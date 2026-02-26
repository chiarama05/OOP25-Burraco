package player;
import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class PlayerImpl implements Player{

    /** The cards currently in the player's hand */
    private List<Card> hand = new ArrayList<>();
    private List<Card> pot = new ArrayList<>();
    private List<List<Card>> combinations = new ArrayList<>();

    /** Flag to track if the player is playing from the pozzetto */
    private boolean inPot = false;

    /**
     * Returns the cards in the player's hand.
     */
    @Override
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
     */
    @Override
    public void addCardHand(Card c) {
        hand.add(c);
    }

    /**
     * Removes a card from the player's hand.
     */
    @Override
    public void removeCardHand(Card c) {
        hand.remove(c);
    }

    /**
     * Checks if the player's hand is empty.
     */
    @Override
    public boolean emptyHand() {
        return hand.isEmpty();
    }

    /**
     * Checks if the player is currently in the pot.
     */
    @Override
    public boolean isInPot() {
        return inPot;
    }

    /**
     * Sets whether the player is in the pot.
     */
    @Override
    public void setInPot(boolean flag) {
        this.inPot = flag;
    }

    @Override
    public void addCombination(List<Card> comb) {
        combinations.add(new ArrayList<>(comb));
    }

    @Override
    public List<List<Card>> getCombinations() {
        return combinations;
    }

    @Override
    public int getNumberBurraco() {
        return (int) combinations.stream()
                .filter(c -> c.size() >= 7)
                .count();
    }

    @Override
    public void drawPot() {
        hand.addAll(pot);
        pot.clear();
    }

    @Override
    public boolean hasFinishedCards() {
        return hand.isEmpty();
    }

    /** Adds a list of cards to the pot */
    public void addToPot(List<Card> cards) {
        pot.addAll(cards);
    }

    /** Returns the current pot cards */
    public List<Card> getPot() {
        return pot;
    }
}
