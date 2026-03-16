package model.turn;

import model.player.Player;

public class TurnImpl implements Turn{

    private final Player player1;
    private final Player player2;
    private boolean isPlayer1Turn = true;
    private boolean gameFinished = false;

    public TurnImpl(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
    }

    @Override
    public void nextTurn() {
        this.isPlayer1Turn = !isPlayer1Turn;
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
        Player p = getCurrentPlayer();
        return p.isInPot() && p.getBurracoCount() >= 1;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean finished) {
        this.gameFinished = finished;
    }

    public Player getPlayer1() { 
        return player1; 
    }

    public Player getPlayer2() { 
        return player2; 
    }
}
