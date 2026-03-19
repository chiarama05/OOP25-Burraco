package it.unibo.burraco.core.discardcard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.core.closure.ClosureState;
import it.unibo.burraco.core.closure.ClosureValidator;

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


        ClosureState state=ClosureValidator.evaluateAfterDiscard(player);

        switch (state){

             // Player discarded last card with pot + burraco → round over.
            case ROUND_WON:
                return new DiscardResult(true, false, true, "Victory!");

            //if player try to discard last card without a Burraco undo the discard so the model stays consistent.
            case CANNOT_CLOSE_NO_BURRACO:
                discardPile.drawLast();
                player.addCardHand(card);
                return new DiscardResult(false, false, false,"You need at least one Burraco to close the round!");

            case OK:

            //If the hand is now empty AND the pot was not yet taken,signal the controller to trigger the pot draw.
            default:
                boolean potNeeded = player.getHand().isEmpty() && !player.isInPot();
                return new DiscardResult(true, !potNeeded, false,potNeeded ? "Pot taken!" : "Discard done.");
        }

        
        // ##
        /*if (player.isInPot() && player.hasFinishedCards()) {
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

       
        return new DiscardResult(true, true, false, "Discard done.");*/
    
    }
}
