package it.unibo.burraco.controller.attach;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.combination.straight.StraightUtils;
import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;

/**
 * Controller class that manages the action of a player attaching cards to a combination.
 * It validates game state, handles the execution of the move, and returns detailed results.
 */
public class AttachController {

    private static final int BURRACO_SIZE = 7;

    /**
     * Default constructor for AttachController.
     */
    public AttachController() { }

    /**
     * Attempts to attach selected cards to a combination.
     * Validates player turn, draw status and game rules.
     *
     * @param currentPlayer the player performing the action
     * @param selectedCards the cards the player wants to play
     * @param combinationCards the target combination on the table
     * @param hasDrawn whether the player has already drawn a card this turn
     * @param isCurrentPlayer whether it is the player's turn
     * @return an {@link AttachResult} indicating success or the specific reason for failure
     */
    public AttachResult tryAttach(final Player currentPlayer,
                                   final List<Card> selectedCards,
                                   final List<Card> combinationCards,
                                   final boolean hasDrawn,
                                   final boolean isCurrentPlayer) {

        if (!hasDrawn) {
            return AttachResult.NOT_DRAWN;
        }
        if (!isCurrentPlayer) {
            return AttachResult.WRONG_PLAYER;
        }
        if (selectedCards.isEmpty()) {
            return AttachResult.NO_CARDS_SELECTED;
        }

        final List<Card> hypotheticalCards = new ArrayList<>(combinationCards);
        hypotheticalCards.addAll(selectedCards);

        List<Card> hypothetical = hypotheticalCards;

        if (StraightUtils.isSameSeed(combinationCards)) {
            hypothetical = StraightUtils.orderStraight(hypothetical);
        } else {
            hypothetical.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

        if (!CombinationValidator.isValidCombination(hypothetical)) {
            return AttachResult.INVALID_COMBINATION;
        }

        if (ClosureValidator.wouldGetStuckAfterAttach(currentPlayer, selectedCards, combinationCards.size())) {
            return AttachResult.WOULD_GET_STUCK;
        }

        final int sizeBefore = combinationCards.size();
        final boolean success = this.executeAttach(currentPlayer, selectedCards, combinationCards);

        if (!success) {
            return AttachResult.ATTACH_FAILED;
        }

        if (sizeBefore < BURRACO_SIZE && combinationCards.size() >= BURRACO_SIZE) {
            return AttachResult.SUCCESS_BURRACO;
        }

        final ClosureState state = ClosureValidator.evaluate(currentPlayer);

        if (state == ClosureState.ZERO_CARDS_NO_POT) {
            return AttachResult.SUCCESS_TAKE_POT;
        } else if (state == ClosureState.CAN_CLOSE) {
            return AttachResult.SUCCESS_CLOSE;
        } else if (state == ClosureState.ZERO_CARDS_NO_BURRACO) {
            return AttachResult.SUCCESS_STUCK;
        } else {
            return AttachResult.SUCCESS;
        }
    }

    /**
     * Performs the actual update of the game state:
     * updates the combination on the table and removes cards from the player's hand.
     *
     * @param player the player performing the move
     * @param selectedCards the cards to remove from hand
     * @param combinationCards the combination to update
     * @return true if the execution was successful, false if validation failed
     */
    private boolean executeAttach(final Player player,
                                   final List<Card> selectedCards,
                                   final List<Card> combinationCards) {
        if (!AttachUtils.canAttach(combinationCards, selectedCards)) {
            return false;
        }

        combinationCards.addAll(selectedCards);

        for (final List<Card> playerComb : player.getCombinations()) {
            if (!combinationCards.isEmpty() && playerComb.contains(combinationCards.get(0))) {
                playerComb.clear();
                playerComb.addAll(combinationCards);
                break;
            }
        }

        player.removeCards(selectedCards);
        return true;
    }
}
