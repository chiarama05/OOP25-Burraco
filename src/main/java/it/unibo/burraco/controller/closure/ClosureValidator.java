package it.unibo.burraco.controller.closure;

import java.util.List;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.Card;

/**
 * Utility class to validate the state of the game regarding round closure.
 * It ensures that players follow the rules for taking the pot and closing the round.
 */
public final class ClosureValidator {

    private static final int BURRACO_THRESHOLD = 7;

    /**
     * Constructs a new ClosureValidator.
     */
    public ClosureValidator() {
        // Default constructor
    }

    /**
     * Evaluates the full closure state of a player after any action.
     * The evaluation order is: hand not empty -> OK; no pot -> ZERO_CARDS_NO_POT;
     * no burraco -> ZERO_CARDS_NO_BURRACO; all conditions met -> CAN_CLOSE.
     *
     * @param player the player to evaluate
     * @return the appropriate ClosureState
     */
    public ClosureState evaluate(final Player player) {
        final boolean handEmpty = player.getHand().isEmpty();
        if (!handEmpty) {
            return ClosureState.OK;
        }
        if (!player.isInPot()) {
            return ClosureState.ZERO_CARDS_NO_POT;
        }
        if (player.getBurracoCount() < 1) {
            return ClosureState.ZERO_CARDS_NO_BURRACO;
        }
        return ClosureState.CAN_CLOSE;
    }

    /**
     * Checks whether the player can legally end the round by discarding their last card.
     * All three conditions must hold: taken the pot, has a burraco, and has exactly one card.
     *
     * @param player the player to check
     * @return true if the player is allowed to discard their last card and close
     */
    public boolean canCloseByDiscarding(final Player player) {
        return player.isInPot() && player.getBurracoCount() >= 1 && player.getHand().size() == 1;
    }

    /**
     * Evaluates the closure state immediately after a discard has been performed.
     *
     * @param player the player who just discarded
     * @return the appropriate ClosureState
     */
    public ClosureState evaluateAfterDiscard(final Player player) {
        if (player.getHand().isEmpty() && player.isInPot()) {
            return player.getBurracoCount() >= 1 ? ClosureState.ROUND_WON : ClosureState.CANNOT_CLOSE_NO_BURRACO;
        }
        return ClosureState.OK;
    }

    /**
     * Determines whether placing a combination would leave the player in an illegal state.
     *
     * @param player      the player attempting the move
     * @param cardsToPlay the cards the player intends to place
     * @param comboSize   the total number of cards in the new combination
     * @return true if the move would leave the player stuck, false otherwise
     */
    public boolean wouldGetStuckAfterPutCombo(
            final Player player,
            final List<Card> cardsToPlay,
            final int comboSize) {

        if (!player.isInPot()) {
            return false;
        }
        final int handAfter = player.getHand().size() - cardsToPlay.size();
        return handAfter == 0
            || handAfter == 1 && !(player.getBurracoCount() >= 1 || comboSize >= BURRACO_THRESHOLD);
    }

    /**
     * Determines whether attaching cards would leave the player in an illegal state.
     *
     * @param player           the player attempting the attach
     * @param cardsToAttach    the cards the player wants to attach
     * @param currentComboSize the current number of cards in the target combination
     * @return true if the attach would leave the player stuck, false otherwise
     */
    public boolean wouldGetStuckAfterAttach(
            final Player player,
            final List<Card> cardsToAttach,
            final int currentComboSize) {

        if (!player.isInPot()) {
            return false;
        }
        final int handAfter = player.getHand().size() - cardsToAttach.size();
        return handAfter == 0
            || handAfter == 1
            && !(player.getBurracoCount() >= 1
            || currentComboSize + cardsToAttach.size() >= BURRACO_THRESHOLD);
    }
}
