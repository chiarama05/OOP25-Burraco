package it.unibo.burraco.controller.discardcard.discard;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;

/**
 * Interface defining the business logic for the discard action.
 * It focuses strictly on game rules and state transitions.
 */
public interface DiscardManager {

    /**
     * Executes the discard operation for a specific player.
     * 
     * @param player the player performing the discard.
     * @param card the card to be moved from hand to discard pile.
     * @return a {@link DiscardResult} describing the outcome and potential win state.
     */
    DiscardResult discard(Player player, Card card);

}
