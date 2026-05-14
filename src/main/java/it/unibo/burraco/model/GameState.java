package it.unibo.burraco.model;

import it.unibo.burraco.model.cards.Card;
import java.util.List;

/**
 * An immutable snapshot of the current game status.
 * It packages all information required by the View to render the game board,
 * including player combinations, the active hand, and the discard pile.
 */
public final class GameState {
    private final List<List<Card>> p1Combinations;
    private final List<List<Card>> p2Combinations;
    private final boolean isP1Turn;
    private final List<Card> currentHand;
    private final List<Card> discardPile;

    /**
     * Constructs a GameState snapshot.
     * 
     * @param p1Combinations all sets and sequences placed on the table by Player 1
     * @param p2Combinations all sets and sequences placed on the table by Player 2
     * @param isP1Turn       true if it is currently Player 1's turn
     * @param currentHand    the cards held by the player currently taking the turn
     * @param discardPile    the current state of the discard pile
     */
    public GameState(
            final List<List<Card>> p1Combinations,
            final List<List<Card>> p2Combinations,
            final boolean isP1Turn,
            final List<Card> currentHand,
            final List<Card> discardPile) {
        this.p1Combinations = p1Combinations;
        this.p2Combinations = p2Combinations;
        this.isP1Turn = isP1Turn;
        this.currentHand = currentHand;
        this.discardPile = discardPile;
    }

    /**
     * @return the combinations belonging to Player 1.
     */
    public List<List<Card>> getP1Combinations() {
        return p1Combinations;
    }

    /**
     * @return the combinations belonging to Player 2.
     */
    public List<List<Card>> getP2Combinations() {
        return p2Combinations;
    }

    /**
     * @return true if the turn belongs to Player 1, false for Player 2.
     */
    public boolean isP1Turn() {
         return isP1Turn;
    }

    /**
     * @return the hand of the player whose turn is active.
     */
    public List<Card> getCurrentHand() {
        return currentHand;
    }

    /**
     * @return the list of cards currently in the discard pile.
     */
    public List<Card> getDiscardPile() {
        return discardPile;
    }
}