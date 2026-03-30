package it.unibo.burraco.model.turn;

import it.unibo.burraco.model.player.Player;

/**
 * Defines the contract for managing turn alternation in a Burraco game.
 */
public interface Turn {

    /**
     * Advances the game to the next player's turn.
     */
    void nextTurn();

    /**
     * Returns the player whose turn it currently is.
     * @return the current player.
     */
    Player getCurrentPlayer();

    /**
     * Checks if it is currently player 1's turn.
     * @return true if it is player 1's turn, false otherwise.
     */
    boolean isPlayer1Turn();

    /**
     * Checks if the current player meets the conditions to close the round.
     * @return true if the current player can close, false otherwise.
     */
    boolean canClose();

    /**
     * Checks if the game has been marked as finished.
     * @return true if the game is finished, false otherwise.
     */
    boolean isGameFinished();

    /**
     * Sets the game finished state.
     * @param finished true to mark the game as finished, false otherwise.
     */
    void setGameFinished(final boolean finished);

    /**
     * Resets the turn state for a new round.
     */
    void resetForNewRound();

    /**
     * Returns the first player.
     * @return player 1.
     */
    Player getPlayer1();

    /**
     * Returns the second player.
     * @return player 2.
     */
    Player getPlayer2();
}