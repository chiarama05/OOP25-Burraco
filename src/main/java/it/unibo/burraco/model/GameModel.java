package it.unibo.burraco.model;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.discard.DiscardPile;

/**
 * Facade interface for the game model.
 * It coordinates players, deck, discard pile and turn logic.
 */
public interface GameModel {

    /**
     * Initializes the game: creates deck, players and distributes initial cards.
     */
    void initializeGame();

    /**
     * Draws a card from the deck for the current player.
     * @return the drawn card.
     */
    Card drawFromDeck();

    /**
     * Makes the current player draw the entire discard pile.
     */
    void drawFromDiscardPile();

    /**
     * Current player discards a card.
     * @param card the card to discard.
     */
    void discardCard(Card card);

    /**
     * Transitions to the next player's turn.
     */
    void nextTurn();

    /**
     * @return the player whose turn it is.
     */
    Player getCurrentPlayer();

    /** @return the first player. */
    Player getPlayer1();

    /** @return the second player. */
    Player getPlayer2();

    /** @return the shared deck. */
    Deck getCommonDeck();

    /** @return the discard pile. */
    DiscardPile getDiscardPile();

    /** @return the turn manager. */
    Turn getTurn();

    /**
     * Checks if the player is Player 1.
     * @param player player to check.
     * @return true if it's P1.
     */
    boolean isPlayer1(Player player);
    
    /** @return true if a victory condition is met. */
    boolean isGameOver();
}
