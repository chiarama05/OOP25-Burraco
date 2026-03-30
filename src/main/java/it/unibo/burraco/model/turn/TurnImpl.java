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

    @Override
    public void nextTurn() {
        this.isPlayer1Turn = !this.isPlayer1Turn;
    }

    @Override
    public void resetForNewRound() {
        this.isPlayer1Turn = true;
        this.gameFinished = false;
    }

    @Override
    public Player getCurrentPlayer() {
        return isPlayer1Turn ? player1 : player2;
    }

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
