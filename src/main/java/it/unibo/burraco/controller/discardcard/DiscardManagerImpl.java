package it.unibo.burraco.controller.discardcard;

import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import java.util.List;

public class DiscardManagerImpl implements DiscardManager {

    private final DiscardPile discardPile;

    public DiscardManagerImpl(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    
    public List<Card> getDiscardPile() {
        return discardPile.getCards();
    }

    @Override
    public DiscardResult discard(Player player, Card card) {

        if (card == null) {
            return DiscardResult.error("You must select a card to discard.");
        }

        if (!player.getHand().contains(card)) {
            return DiscardResult.error("The selected card is not in the player's hand.");
        }

        player.removeCardHand(card);
        discardPile.add(card);

        ClosureState state = ClosureValidator.evaluateAfterDiscard(player);

        switch (state) {

            case ROUND_WON:
                return DiscardResult.success(discardPile.getCards(), player, true);

            case CANNOT_CLOSE_NO_BURRACO:
                // Undo — modello rimane consistente
                discardPile.drawLast();
                player.addCardHand(card);
                return DiscardResult.error("You need at least one Burraco to close the round!");

            case OK:
            default:
                return DiscardResult.success(discardPile.getCards(), player, false);
        }
    }
}
