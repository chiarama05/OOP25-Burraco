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
    * <p>
    * The evaluation order is: hand not empty → OK; no pot → ZERO_CARDS_NO_POT;
    * no burraco → ZERO_CARDS_NO_BURRACO; all conditions met → CAN_CLOSE.
    * </p>
    *
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

    /**
    * Checks whether the player can legally end the round by discarding their last card.
    * <p>
    * All three conditions must hold simultaneously:
    * the player has taken the pot, has at least one burraco, and has exactly one card left.
    * </p>
    *
    * @param player the player to check
    * @return {@code true} if the player is allowed to discard their last card and close
    */
    public static boolean canCloseByDiscarding(final Player player) {
        return player.isInPot() && player.getBurracoCount() >= 1 && player.getHand().size() == 1;
    }



    /**
    * Evaluates the closure state immediately after a discard has been performed.
    * <p>
    * Returns {@link ClosureState#ROUND_WON} only when the player's hand is empty,
    * the pot has been taken, and at least one burraco exists.
    * Returns {@link ClosureState#CANNOT_CLOSE_NO_BURRACO} if the hand is empty but
    * the burraco requirement is not satisfied.
    * Returns {@link ClosureState#OK} in all other cases.
    * </p>
    *
    * @param player the player who just discarded
    * @return the appropriate {@link ClosureState}
    */
    public static ClosureState evaluateAfterDiscard(final Player player) {
        if (player.getHand().isEmpty() && player.isInPot()) {
            return player.getBurracoCount()>=1 ? ClosureState.ROUND_WON : ClosureState.CANNOT_CLOSE_NO_BURRACO;
        }
        return ClosureState.OK;
    }


    /**
    * Determines whether placing a combination would leave the player in an illegal state
    * from which they cannot proceed (no cards left, no pot available, no burraco formed).
    *
    * @param player       the player attempting the move
    * @param cardsToPlay  the cards the player intends to place
    * @param comboSize    the total number of cards in the new combination (used to detect burraco)
    * @return {@code true} if the move would leave the player stuck, {@code false} otherwise
    */
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


    /**
    * Determines whether attaching cards to an existing combination would leave the player
    * in an illegal state from which they cannot proceed.
    *
    * @param player           the player attempting the attach
    * @param cardsToAttach    the cards the player wants to attach
    * @param currentComboSize the current number of cards in the target combination
    *                         (used to determine if the attach completes a burraco)
    * @return {@code true} if the attach would leave the player stuck, {@code false} otherwise
    */
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
