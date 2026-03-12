package core.turnvalidation;

import model.player.Player;

public class TurnValidatorImpl implements TurnValidator {

    private final boolean allowPotFly;

    public TurnValidatorImpl() {
        this(true);
    }

    public TurnValidatorImpl(boolean allowPotFly) {
        this.allowPotFly = allowPotFly;
    }

    @Override
    public void startTurn(Player player){
        //
    }

    @Override
    public TurnPlayOutcome canPlayCardsNow(Player player, int cardsToPlay) {
        if (cardsToPlay <= 0) {
            return TurnPlayOutcome.deny("No cards selected.");
        }

        final int hand = player.getHand().size();
        final int remaining = hand - cardsToPlay;

       
        if (remaining >= 1) {
            return TurnPlayOutcome.allow();
        }

        if (remaining < 0) {
            return TurnPlayOutcome.deny("You don't have enough cards.");
        }

        
        if (!player.isInPot()) {
            if (allowPotFly) {
                return TurnPlayOutcome.allowWithPotFly();
            } 
            else {
                return TurnPlayOutcome.deny("You must keep one card to discard.");
            }
        }


        return TurnPlayOutcome.deny("You must keep one card to discard.");
    }
}