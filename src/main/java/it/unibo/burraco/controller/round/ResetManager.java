package it.unibo.burraco.controller.round;

import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.player.Player;

/**
 * Interface responsible for resetting the game state components
 * to prepare for a new round of play.
 */
@FunctionalInterface
public interface ResetManager {

    /**
     * Resets players, the deck, and the discard pile.
     *
     * @param p1 the first player.
     * @param p2 the second player.
     * @param deck the game deck.
     * @param discardPile the discard pile.
     */
    void resetRound(Player p1,
                    Player p2,
                    Deck deck,
                    DiscardPile discardPile);
}
