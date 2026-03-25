package it.unibo.burraco.controller.discardcard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

public class DiscardResult {

    private final boolean valid;
    private final boolean turnEnds;
    private final boolean gameWon;
    private final String message;
    private final List<Card> updatedDiscardPile; 
    private final Player currentPlayer;         

    public DiscardResult(boolean valid, boolean turnEnds, boolean gameWon,
                          String message, List<Card> updatedDiscardPile, Player currentPlayer) {
        this.valid = valid;
        this.turnEnds = turnEnds;
        this.gameWon = gameWon;
        this.message = message;
        this.updatedDiscardPile = updatedDiscardPile;
        this.currentPlayer = currentPlayer;
    }

    public static DiscardResult error(String message) {
        return new DiscardResult(false, false, false, message, null, null);
    }

    public static DiscardResult success(List<Card> pile, Player player, boolean gameWon) {
        return new DiscardResult(true, true, gameWon, null, pile, player);
    }

    public boolean isValid()                    { return valid; }
    public boolean isTurnEnds()                 { return turnEnds; }
    public boolean isGameWon()                  { return gameWon; }
    public String getMessage()                  { return message; }
    public List<Card> getUpdatedDiscardPile()   { return updatedDiscardPile; }
    public Player getCurrentPlayer()            { return currentPlayer; }
}