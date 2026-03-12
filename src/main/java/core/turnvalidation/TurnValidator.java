package core.turnvalidation;
import model.player.Player;

public interface TurnValidator {

    void startTurn(Player player);

    TurnPlayOutCome canPlayCardsNow(Player player, int cardsToPlay);

    default void onCardsPlayed(Player player, int cardsPlayed){

    }   
}
