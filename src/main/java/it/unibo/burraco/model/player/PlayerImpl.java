package it.unibo.burraco.model.player;

import java.util.ArrayList;
import java.util.List;
import it.unibo.burraco.model.card.Card;

/**
 * Implementation of the {@link Player} interface.
 * Represents a player in the Burraco game.
 */
public class PlayerImpl implements Player {

    private final List<Card> hand = new ArrayList<>();
    private final List<Card> pot = new ArrayList<>();
    private final List<List<Card>> combinations = new ArrayList<>();
    private final String name;

    private boolean inPot;
    private int matchTotalScore;

    /**
     * Constructs a PlayerImpl with default name "Player".
     */
    public PlayerImpl() {
        this.name = "Player";
    }

    /**
     * Constructs a PlayerImpl with the specified name.
     * @param name the player's name
     */
    public PlayerImpl(final String name) {
        this.name = name;
    }

    @Override
    public void addPointsToMatch(final int points) {
        this.matchTotalScore += points;
    }

    @Override
    public int getMatchTotalScore() {
        return this.matchTotalScore;
    }

    @Override
    public void resetForNewRound() {
        this.hand.clear();
        this.combinations.clear();
        this.pot.clear();
        this.setInPot(false);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }

    @Override
    public void addCardHand(final Card c) {
        hand.add(c);
    }

    @Override
    public void removeCardHand(final Card c) {
        hand.remove(c);
    }

    @Override
    public void removeCards(final List<Card> cards) {
        for (final Card c : cards) {
            removeCardHand(c);
        }
    }

    @Override
    public boolean isInPot() {
        return inPot;
    }

    @Override
    public void setInPot(final boolean flag) {
        this.inPot = flag;
    }

    @Override
    public void addCombination(final List<Card> comb) {
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
        if (!pot.isEmpty()) {
            this.hand.addAll(new ArrayList<>(pot));
            this.pot.clear();
            this.inPot = true;
        }
    }

    @Override
    public boolean hasFinishedCards() {
        return hand.isEmpty();
    }

    @Override
    public void addToPot(final List<Card> cards) {
        this.pot.clear();
        this.pot.addAll(cards);
    }

    /**
     * Returns the current pot cards.
     * @return the pot cards
     */
    public List<Card> getPot() {
        return pot;
    }

    @Override
    public boolean hasCard(final Card card) {
        return hand.contains(card);
    }
}