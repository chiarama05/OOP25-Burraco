package core.turnvalidation;

import model.player.Player;

public interface TurnValidator {
    
    
    TurnPlayOutcome canPlayCardsNow(Player player, int cardsToPlay);


    default void onCardsPlayed(Player player, int cardsPlayed) { 
    //
    }

    void startTurn(Player player);
}