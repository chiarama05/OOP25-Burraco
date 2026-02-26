package core.discardcard;
import model.card.Card;
import model.player.Player;

/**
 * Handles the discard logic of the game.
 * Contains only business rules, no GUI code.
 */
public interface DiscardManager {

    /**
     * Executes a discard action for a player.
     *
     * @param player the current player
     * @param card the card to discard
     * @return the result of the discard operation
     */
    
    DiscardResult discard(Player player, Card card);

}
