package core.closure;

import model.player.Player;
import model.card.Card;

import java.util.List;

public class ClosureValidator {
    
     private ClosureValidator() {
    }

    
    public static ClosureState evaluate(Player player) {

        boolean handEmpty=player.getHand().isEmpty();
        boolean hasPot=player.isInPot();
        boolean hasBurraco=player.getBurracoCount()>= 1;


        if (!handEmpty) {
            return ClosureState.OK;
        }

        // Hand is empty from here on

        // Must take the pot before anything else can happen.
        if (!hasPot) {
            return ClosureState.ZERO_CARDS_NO_POT;
        }


        // if pot taken, hand empty, but no burraco 
        // formed yet → player is stuck and must be warned 
        if (!hasBurraco) {
            return ClosureState.ZERO_CARDS_NO_BURRACO;
        }

        // Pot taken, hand empty, at least one Burraco → can close!
        return ClosureState.CAN_CLOSE;
    }


    public static boolean canCloseByDiscarding(Player player) {
        return player.isInPot() && player.getBurracoCount() >= 1 && player.getHand().size() == 1;
    }

    
    public static ClosureState evaluateAfterDiscard(Player player) {
        if (player.getHand().isEmpty() && player.isInPot()) {
            return player.getBurracoCount()>=1 ? ClosureState.ROUND_WON : ClosureState.CANNOT_CLOSE_NO_BURRACO;
        }
        return ClosureState.OK;
    }


    /*simultes what happens if the player puts down as a new combination
    
    Returns true if the action would leave the player in an unresolvable
     state:
       - the hand would have exactly 1 card left (only option = discard)
       - AND the player has no burraco yet (cannot legally close)
       - AND the pot is already taken (cannot take pot to get more cards)
       - AND the combination being played does NOT itself become a burraco
    
    The special case where the combination itself is >= 7 (instant burraco)
    is allowed even if 1 card remains, because that combination IS the burraco.
    */
    public static boolean wouldGetStuckAfterPutCombo(Player player,List<Card> cardsToPlay,int comboSize) {
        
        int handAfter= player.getHand().size()-cardsToPlay.size();
        boolean potTaken= player.isInPot();
        boolean hasBurraco= player.getBurracoCount()>= 1;
        boolean newIsBurraco = comboSize>= 7;

        // If the combination itself IS a burraco, the player will have a burraco
        // after this action — no stuck state possible.
        if (newIsBurraco || hasBurraco){
            return false;
        } 

        // No burraco, pot already taken, and only 1 card would remain → stuck.
        if (potTaken && handAfter == 1){
            return true;
        } 

        // No burraco, pot NOT taken yet, hand would be empty → will take pot, not stuck
        if (!potTaken && handAfter == 0){
            return false;
        } 

        // No burraco, pot already taken, hand would be empty → stuck (same as ZERO_CARDS_NO_BURRACO
        if (potTaken && handAfter == 0){
            return true;
        } 

        return false;
    }

    /*
    * Simulates what happens if the player attaches to an existing combination
    
    Returns true if the action would leave the player stuck, using the
    same logic as the method above
    */
    public static boolean wouldGetStuckAfterAttach(Player player,List<Card> cardsToAttach,int currentComboSize) {
        
        int handAfter=player.getHand().size()-cardsToAttach.size();
        boolean potTaken=player.isInPot();
        boolean hasBurraco= player.getBurracoCount() >= 1;
        int comboSizeAfter= currentComboSize+cardsToAttach.size();
        boolean attachMakesBurraco= (currentComboSize< 7 && comboSizeAfter>= 7);

        // If the player already has a burraco, or this attach creates one → it's safe.
        if (hasBurraco || attachMakesBurraco){
            return false;
        } 

        // No burraco after attach, pot taken, 1 card left → stuck.
        if (potTaken && handAfter == 1){
            return true;
        } 

        // No burraco after attach, pot taken, 0 cards left → stuck.
        if (potTaken && handAfter == 0){
             return true;
        }

        return false;
    }

}
