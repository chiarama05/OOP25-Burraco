package it.unibo.burraco.controller.discardcard.discard;

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
        return DiscardResult.error("NOT_SELECTED");
    }

    if (!player.getHand().contains(card)) {
        return DiscardResult.error("NOT_IN_HAND");
    }

    // 2. Logica di scarto
    player.removeCardHand(card);
    discardPile.add(card);

    // 3. Controllo chiusura
    ClosureState state = ClosureValidator.evaluateAfterDiscard(player);

    switch (state) {
        case ROUND_WON:
            return DiscardResult.success(discardPile.getCards(), player, true);

        case CANNOT_CLOSE_NO_BURRACO:
            // Rollback: riporto lo stato a prima dello scarto
            discardPile.drawLast();
            player.addCardHand(card);
            // Restituiamo solo l'identificativo dell'errore
            return DiscardResult.error("NO_BURRACO_ERROR");

        case OK:
        default:
            return DiscardResult.success(discardPile.getCards(), player, false);
        }
    }
}
