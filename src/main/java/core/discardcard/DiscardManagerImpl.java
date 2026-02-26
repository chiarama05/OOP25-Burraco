package core.discardcard;

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
            return new DiscardResult(false, false, false,
                    "You must select a card to discard.");
        }

        if(!player.getHand().contains(card)){
            return new DiscardResult(false, false, false,
                    "The selected card is not in the player's hand.");
        }

        int handSizeBefore = player.getHand().size();

        // Closing rule after taking the pot
        if (player.isInPot()) {
            if (handSizeBefore <= 1 &&
                player.getBurracoCount() < 1) {

                return new DiscardResult(false, false, false,
                        "You need at least one Burraco to close the game.");
            }
        }

        // Perform discard
        player.removeCardHand(card);
        discardPile.add(card);

        boolean closingAttempt = player.isInPot() && player.hasFinishedCards();

        // Check win condition
        if (closingAttempt) {

            if (player.getBurracoCount() >= 1) {
                return new DiscardResult(true, false, true,
                        "Game closed successfully. You win!");
            }else{
                // Undo discard
                discardPile.drawLast();
                player.addCardHand(card);

                return new DiscardResult(false, false, false,
                        "Invalid closing attempt. At least one Burraco is required.");
            }
        }
        // Player finished hand without closing → take pot
        if (player.hasFinishedCards() &&
            !player.isInPot()) {

            player.setInPot(true);

            return new DiscardResult(true, true, false,
                    "Pozzetto taken. Turn ends.");
        }

        return new DiscardResult(true, true, false,
                "Card discarded successfully."); 
    }
}
