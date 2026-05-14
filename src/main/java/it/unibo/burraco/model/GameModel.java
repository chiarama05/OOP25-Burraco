package it.unibo.burraco.model;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;

/**
 * Facade interface representing the core Game Model.
 * It acts as the single point of access for the Controller to interact with 
 * the game state, coordinating the deck, discard pile, players, and turn logic.
 */
public interface GameModel {

    /**
     * Advances the game state to the next player's turn.
     */
    void nextTurn();

    /**
     * @return the {@link Player} whose turn is currently active.
     */
    Player getCurrentPlayer();

    /**
     * @return the first player of the match.
     */
    Player getPlayer1();

    /**
     * @return the second player of the match.
     */
    Player getPlayer2();

    /**
     * @return the shared {@link Deck} used in the game.
     */
    Deck getCommonDeck();

    /**
     * @return the current {@link DiscardPile}.
     */
    DiscardPile getDiscardPile();

    /**
     * @return the current {@link Turn} management object.
     */
    Turn getTurn();

    /**
     * Helper method to identify if a specific player is Player 1.
     * 
     * @param player the player to check
     * @return true if the provided player is player 1, false otherwise.
     */
    boolean isPlayer1(Player player);

    /**
     * Checks if the match has reached its end conditions.
     * 
     * @return true if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * Verifies if a move is legal according to Burraco rules without changing the state.
     * 
     * @param move the attempted {@link Move}
     * @return a {@link MoveResult} containing the validation status.
     */
    MoveResult validateMove(Move move);

    /**
     * Executes a move and updates the game state.
     * 
     * @param move the {@link Move} to be applied
     * @return the {@link MoveResult} representing the outcome of the execution.
     */
    MoveResult applyMove(Move move);

    /**
     * @return the {@link Player} who won the match, or null if the game is still ongoing.
     */
    Player getWinner();

    /**
     * Checks if the current player has already drawn a card in the current turn.
     * 
     * @return true if a card has been drawn, false otherwise.
     */
    boolean hasDrawn();

    /**
     * Resets the entire game model state to start a fresh round, 
     * preserving only the long-term match data (like cumulative scores).
     */
    void resetForNewRound();
}
