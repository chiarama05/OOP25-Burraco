package model.player;
import java.util.ArrayList;
import java.util.List;
import core.combination.*;
import model.card.Card;


public class PlayerImpl implements Player{

    //public static final String DEFAULT1="Player 1";
    //public static final String DEFAULT2="Player 2";

    /** The cards currently in the player's hand */
    private List<Card> hand = new ArrayList<>();
    private List<Card> pot = new ArrayList<>();
    private List<List<Card>> combinations = new ArrayList<>();

    private String name;

    private boolean inPot = false;

    private int matchTotalScore = 0;
    public void addPointsToMatch(int points) {
        this.matchTotalScore += points;
    }

    public int getMatchTotalScore() {
        return this.matchTotalScore;
    }
    
    // Metodo per resettare le carte ma non i punti tra un round e l'altro
    public void resetForNewRound() {
        this.hand.clear();
        this.combinations.clear();
        this.pot.clear();
        this.setInPot(false);
    }

    public PlayerImpl() {
        this.name = "Giocatore";
    }


    public PlayerImpl(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

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

    @Override
    public void removeCards(List<Card> cards) {
    for (Card c : cards) {
        removeCardHand(c); // usa il metodo già presente
    }
}

    /**
     * Checks if the player's hand is empty.
     */
   /* @Override
    public boolean emptyHand() {
        return hand.isEmpty();
    }*/

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
    public int getBurracoCount() {
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

    @Override
    public void addToPot(List<Card> cards) {
        pot.addAll(cards);
    }

    /** Returns the current pot cards */
    public List<Card> getPot() {
        return pot;
    }

    @Override
    public boolean hasCard(Card card) {
    return hand.contains(card);
    }
}
