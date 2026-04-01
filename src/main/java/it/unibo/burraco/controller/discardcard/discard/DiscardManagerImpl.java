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
public final class DiscardManagerImpl implements DiscardManager {

    private final DiscardPile discardPile;

    /**
     * Constructs a DiscardManagerImpl.
     *
     * @param discardPile the shared discard pile model
     */
    public DiscardManagerImpl(final DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    /**
     * Returns the current list of cards in the discard pile.
     *
     * @return the current list of cards in the discard pile
     */
    public List<Card> getDiscardPile() {
        return this.discardPile.getCards();
    }

    @Override
    public DiscardResult discard(final Player player, final Card card) {
        // Card selection check
        if (card == null) {
            return DiscardResult.error("NOT_SELECTED");
        }

        if (!player.getHand().contains(card)) {
            return DiscardResult.error("NOT_IN_HAND");
        }

        player.removeCardHand(card);
        this.discardPile.add(card);

        final ClosureState state = ClosureValidator.evaluateAfterDiscard(player);

        switch (state) {
            case ROUND_WON:
                return DiscardResult.success(this.discardPile.getCards(), player, true);
            case CANNOT_CLOSE_NO_BURRACO:
                this.discardPile.drawLast();
                player.addCardHand(card);
                return DiscardResult.error("NO_BURRACO_ERROR");
            case OK:
            default:
                return DiscardResult.success(this.discardPile.getCards(), player, false);
        }
    }
}
