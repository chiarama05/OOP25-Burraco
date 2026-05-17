package it.unibo.burraco.controller.display;

import it.unibo.burraco.model.cards.Card;
import java.util.List;

/**
 * Immutable snapshot of the current game status.
 * Built by the controller and consumed by the view layer.
 * This class follows the same architectural pattern as ScoreSnapshot 
 * in the score controller package.
 */
public final class GameState {

    private final List<List<Card>> p1Combinations;
    private final List<List<Card>> p2Combinations;
    private final boolean isP1Turn;
    private final List<Card> currentHand;
    private final List<Card> discardPile;

    /**
     * Constructs a new GameState snapshot.
     * To guarantee true immutability, the collections provided should ideally be 
     * unmodifiable or defensively copied prior to initialization.
     *
     * @param p1Combinations the list of card combinations melded by player 1
     * @param p2Combinations the list of card combinations melded by player 2
     * @param isP1Turn       true if it is currently player 1's turn, false otherwise
     * @param currentHand    the list of cards currently held in the active player's hand
     * @param discardPile    the list of cards currently present in the discard pile
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
     * Returns the combinations melded by player 1.
     *
     * @return a list of card lists representing player 1's table status
     */
    public List<List<Card>> getP1Combinations() {
        return this.p1Combinations;
    }

    /**
     * Returns the combinations melded by player 2.
     *
     * @return a list of card lists representing player 2's table status
     */
    public List<List<Card>> getP2Combinations() {
        return this.p2Combinations;
    }

    /**
     * Checks if it is currently player 1's turn.
     *
     * @return true if it is player 1's turn, false if it is player 2's turn
     */
    public boolean isP1Turn() {
        return this.isP1Turn;
    }

    /**
     * Returns the hand of the current active player.
     *
     * @return a list of cards currently held by the player whose turn it is
     */
    public List<Card> getCurrentHand() {
        return this.currentHand;
    }

    /**
     * Returns the current state of the discard pile.
     *
     * @return a list of cards containing all elements in the discard pile
     */
    public List<Card> getDiscardPile() {
        return this.discardPile;
    }
}
