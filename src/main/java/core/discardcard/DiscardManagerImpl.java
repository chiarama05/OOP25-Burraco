package core.discardcard;

import java.util.List;

import model.card.Card;
import model.discard.DiscardPile;
import model.player.Player;

/**
 * Concrete implementation of the discard logic.
 * Applies all Burraco rules related to discarding.
 */
public class DiscardManagerImpl implements DiscardManager{

    private final DiscardPile discardPile;
    public DiscardManagerImpl(DiscardPile discardPile){
        this.discardPile = discardPile;
    }

    @Override
    public DiscardResult discard(Player player, Card card){

        // Validate selection 
        if(card == null){
            return new DiscardResult(false, false, false, "You must select a card to discard.");
        }

        if(!player.getHand().contains(card)){
            return new DiscardResult(false, false, false, "The selected card is not in the player's hand.");
        }

        player.removeCardHand(card);
        discardPile.add(card);


        
        if (player.isInPot() && player.hasFinishedCards()) {
        if (player.getBurracoCount() >= 1) {
            return new DiscardResult(true, false, true, "Victory!");
        } else {
        
            discardPile.drawLast();
            player.addCardHand(card);
            return new DiscardResult(false, false, false, "Need a Burraco to close!");
        }
        }

        
        if (!player.isInPot() && player.hasFinishedCards()) {
        return new DiscardResult(true, true, false, "Pot taken!");
        }

       
        return new DiscardResult(true, true, false, "Discard done.");
    }
}
