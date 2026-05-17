package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Functional interface responsible for restoring game components
 * to their initial state at the start of a new round.
 */
@FunctionalInterface
public interface ResetManager {

    /**
     * Resets players, the deck, and the discard pile to their default starting state.
     * This method typically involves clearing hands, re-shuffling the deck, 
     * and emptying the discard pile.
     *
     * @param p1 the first player to reset
     * @param p2 the second player to reset
     * @param deck the game deck to reset
     * @param discardPile the discard pile to clear
     */
    void resetRound(Player p1,
                    Player p2,
                    Deck deck,
                    DiscardPile discardPile);
}
