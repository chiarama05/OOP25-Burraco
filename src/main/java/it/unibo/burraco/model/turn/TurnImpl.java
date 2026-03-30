package it.unibo.burraco.model.turn;

import it.unibo.burraco.model.player.Player;

/**
 * Implementation of the {@link Turn} interface.
 * Manages turn alternation and game state for two players.
 */
public final class TurnImpl implements Turn {

    private final Player player1;
    private final Player player2;
    private boolean isPlayer1Turn;
    private boolean gameFinished;

    /**
     * Constructs a TurnImpl with the two players.
     * 
     * @param p1 the first player.
     * @param p2 the second player.
     */
    public TurnImpl(final Player p1, final Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.isPlayer1Turn = true;
        this.gameFinished = false;
    }


    /**
     * Alternates the active player by toggling the {@code isPlayer1Turn} flag.
     * After this call, {@link #getCurrentPlayer()} will return the other player.
     */
    @Override
    public void nextTurn() {
        this.isPlayer1Turn = !this.isPlayer1Turn;
    }


    /**
     * Resets the turn state for a new round.
     * Player 1 is always given the first turn, and the finished flag is cleared.
     */
    @Override
    public void resetForNewRound() {
        this.isPlayer1Turn = true;
        this.gameFinished = false;
    }
    

    @Override
    public Player getCurrentPlayer() {
        return isPlayer1Turn ? player1 : player2;
    }


    /**
     * Checks whether the current player meets the minimum conditions to close the round.
     * <p>
     * Both conditions must hold: the player must have collected the pot
     * ({@link Player#isInPot()}) and must have formed at least one burraco.
     * This method does <em>not</em> check whether the hand is empty.
     * </p>
     *
     * @return {@code true} if the current player can attempt to close
     */
    @Override
    public boolean canClose() {
        final Player p = getCurrentPlayer();
        return p.isInPot() && p.getBurracoCount() >= 1;
    }

    @Override
    public boolean isPlayer1Turn() {
        return this.isPlayer1Turn;
    }

    @Override
    public boolean isGameFinished() {
        return this.gameFinished;
    }

    @Override
    public void setGameFinished(final boolean finished) {
        this.gameFinished = finished;
    }

    @Override
    public Player getPlayer1() {
        return this.player1;
    }

    @Override
    public Player getPlayer2() {
        return this.player2;
    }
}
