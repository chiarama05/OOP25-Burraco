package it.unibo.burraco.controller.discardcard.discard;

import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import java.util.List;

/**
 * Concrete implementation of {@link DiscardManager}.
 * Manages the transition of a card from a player's hand to the discard pile
 * and evaluates the win conditions (Closure) for the current round.
 */
public class DiscardManagerImpl implements DiscardManager {

    private final DiscardPile discardPile;

    /**
     * @param discardPile the shared discard pile model.
     */
    public DiscardManagerImpl(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    /**
     * @return the current list of cards in the discard pile.
     */
    public List<Card> getDiscardPile() {
        return discardPile.getCards();
    }

    @Override
    public DiscardResult discard(Player player, Card card) {
        // Card selection check
        if (card == null) {
            return DiscardResult.error("NOT_SELECTED");
        }

        // Verify the card actually belongs to the player's hand
        if (!player.getHand().contains(card)) {
            return DiscardResult.error("NOT_IN_HAND");
        }   

        // Temporary move of the card to the discard pile
        player.removeCardHand(card);
        discardPile.add(card);

        // Check if this discard results in a round win or an illegal closure
        ClosureState state = ClosureValidator.evaluateAfterDiscard(player);

        switch (state) {
            case ROUND_WON:
                // The player has legally closed the round
                return DiscardResult.success(discardPile.getCards(), player, true);

            case CANNOT_CLOSE_NO_BURRACO:
                /* 
                 * Rollback Logic: 
                 * If the player attempts to discard their last card but lacks a Burraco, 
                 * the action is undone to restore the game state.
                 */
                discardPile.drawLast();
                player.addCardHand(card);
                return DiscardResult.error("NO_BURRACO_ERROR");

            case OK:
            default:
                // Standard discard without closing the round
                return DiscardResult.success(discardPile.getCards(), player, false);
        }
    }
}
