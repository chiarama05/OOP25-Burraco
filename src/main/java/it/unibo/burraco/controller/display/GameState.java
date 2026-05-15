package it.unibo.burraco.controller.display;

import it.unibo.burraco.model.cards.Card;
import java.util.List;

/**
 * Immutable snapshot of the current game status.
 * Built by the controller, consumed by the view.
 * Lives in controller — same pattern as ScoreSnapshot in controller.score.
 */
public final class GameState {

    private final List<List<Card>> p1Combinations;
    private final List<List<Card>> p2Combinations;
    private final boolean isP1Turn;
    private final List<Card> currentHand;
    private final List<Card> discardPile;

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

    public List<List<Card>> getP1Combinations() {
        return p1Combinations;
    }

    public List<List<Card>> getP2Combinations() {
        return p2Combinations;
    }

    public boolean isP1Turn() {
        return isP1Turn;
    }
    public List<Card> getCurrentHand() {
        return currentHand;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }
}