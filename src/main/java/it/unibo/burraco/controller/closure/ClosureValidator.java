package it.unibo.burraco.controller.closure;

import java.util.List;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.Card;

/**
 * Utility class to validate the state of the game regarding round closure.
 * It ensures that players follow the rules for taking the pot and closing the round.
 */
public final class ClosureValidator {
    
    private ClosureValidator() { }


    /**
     * Evaluates the full closure state of a player after any action (put combination or attach).
     * The evaluation order is: hand not empty → OK; no pot → ZERO_CARDS_NO_POT;
     * no burraco → ZERO_CARDS_NO_BURRACO; all conditions met → CAN_CLOSE.
     * @param player the player to evaluate
     * @return the appropriate {@link ClosureState}
     */
    public static ClosureState evaluate(Player player) {
        final boolean handEmpty = player.getHand().isEmpty();
        final boolean hasPot = player.isInPot();
        final boolean hasBurraco = player.getBurracoCount() >= 1;

        if (!handEmpty) {
            return ClosureState.OK;
        }
        if (!hasPot) {
            return ClosureState.ZERO_CARDS_NO_POT;
        }
        if (!hasBurraco) {
            return ClosureState.ZERO_CARDS_NO_BURRACO;
        }
        return ClosureState.CAN_CLOSE;
    }

    public static boolean canCloseByDiscarding(final Player player) {
        return player.isInPot() && player.getBurracoCount() >= 1 && player.getHand().size() == 1;
    }

    public static ClosureState evaluateAfterDiscard(final Player player) {
        if (player.getHand().isEmpty() && player.isInPot()) {
            return player.getBurracoCount()>=1 ? ClosureState.ROUND_WON : ClosureState.CANNOT_CLOSE_NO_BURRACO;
        }
        return ClosureState.OK;
    }

    public static boolean wouldGetStuckAfterPutCombo(final Player player, final List<Card> cardsToPlay, final int comboSize) {
        final int handAfter = player.getHand().size() - cardsToPlay.size();
        final boolean potTaken = player.isInPot();
        final boolean hasBurraco = player.getBurracoCount() >= 1;
        final boolean newIsBurraco = comboSize >= 7;

        if (!potTaken) {
            return false;
        }
        if (handAfter == 0) {
            return true; 
        }
        if (handAfter == 1) {
            if (hasBurraco || newIsBurraco) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean wouldGetStuckAfterAttach(final Player player,final List<Card> cardsToAttach, final int currentComboSize) {
        final int numToAttach = cardsToAttach.size(); 
        final int handAfter = player.getHand().size() - numToAttach;
        final boolean potTaken = player.isInPot();
        final boolean hasBurraco = player.getBurracoCount() >= 1;
        final boolean attachMakesBurraco = (currentComboSize + numToAttach) >= 7;

        if (!potTaken) {
            return false;
        }
        if (handAfter == 0) {
            return true; 
        }
        if (handAfter == 1) {
            if (hasBurraco || attachMakesBurraco) {
                return false; 
            } 
            else {
                return true; 
            }
        }
        return false;
    }
}
