package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Functional interface responsible for restoring game components to their 
 * initial state to prepare for a new round of play.
 * It ensures that all entities are cleared of previous round data and 
 * synchronized for a fresh start.
 */
@FunctionalInterface
public interface ResetManager {

    /**
     * Resets players, the deck, and the discard pile to their default starting state.
     * This method typically involves clearing hands, re-shuffling the deck, 
     * and emptying the discard pile.
     *
     * @param p1          the first player to reset
     * @param p2          the second player to reset
     * @param deck        the game deck to be replenished and reshuffled
     * @param discardPile the discard pile to be cleared
     */
    void resetRound(Player p1,
                    Player p2,
                    Deck deck,
                    DiscardPile discardPile);
}
