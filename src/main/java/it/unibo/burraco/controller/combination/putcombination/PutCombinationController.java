package it.unibo.burraco.controller.combination.putcombination;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.combination.set.SetUtils;
import it.unibo.burraco.controller.combination.straight.StraightUtils;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

/**
 * Controller responsible for managing the logic of placing a new combination on the table.
 * It coordinates with various managers to validate the move, update the player's hand,
 * and trigger game state changes such as taking the pot or closing the round.
 */
public class PutCombinationController {

    private final GameController gameController;
    private final DrawManager drawManager;
    private final PotManager potManager;
    private final ClosureManager closureManager;
    private final Turn turnModel;

    /**
     * Constructs a PutCombinationController with the necessary game components.
     *
     * @param gameController the main game controller for sound and general state
     * @param drawManager manages the draw status of the current turn
     * @param potManager handles the logic for taking the pot
     * @param closureManager manages the end of round or end of game states
     * @param turnModel provides information about the current player and turn
     */
    public PutCombinationController(final GameController gameController,
                                     final DrawManager drawManager,
                                     final PotManager potManager,
                                     final ClosureManager closureManager,
                                     final Turn turnModel) {
        this.gameController = gameController;
        this.drawManager = drawManager;
        this.potManager = potManager;
        this.closureManager = closureManager;
        this.turnModel = turnModel;
    }

    /**
     * Processes the attempt to lay down a new combination of cards.
     * Validates pre-conditions, checks combination rules, and evaluates the
     * consequences on the game state (Burraco, Pot, or Closure).
     *
     * @param selectedCards the list of cards selected by the player to form a new combination
     * @return a {@link PutCombinationResult} object containing the status and details of the action
     */
    public PutCombinationResult tryPutCombination(final List<Card> selectedCards) {
        if (!drawManager.hasDrawn()) {
            return PutCombinationResult.error(PutCombinationResult.Status.NOT_DRAWN);
        }
        if (selectedCards.isEmpty()) {
            return PutCombinationResult.error(PutCombinationResult.Status.NO_CARDS_SELECTED);
        }

        final Player current = turnModel.getCurrentPlayer();

        if (ClosureValidator.wouldGetStuckAfterPutCombo(current, selectedCards, selectedCards.size())) {
            return PutCombinationResult.error(PutCombinationResult.Status.WOULD_GET_STUCK);
        }
        if (selectedCards.size() < 3 || !CombinationValidator.isValidCombination(selectedCards)) {
            return PutCombinationResult.error(PutCombinationResult.Status.INVALID_COMBINATION);
        }

        // Sort the cards if the combination is a Straight
        List<Card> processedCombo = new ArrayList<>(selectedCards);
        if (StraightUtils.isSameSeed(processedCombo) && !SetUtils.isValidSet(processedCombo)) {
            processedCombo = StraightUtils.orderStraight(processedCombo);
        }

        // Execute the move: update player combinations and hand
        current.addCombination(processedCombo);
        current.removeCards(selectedCards);

        // Visual and audio feedback for Burraco
        if (processedCombo.size() >= 7) {
            gameController.getSoundController().playBurracoSound();
        }

        final boolean isPlayer1 = gameController.isPlayer1(current);
        final ClosureState state = ClosureValidator.evaluate(current);

        if (state == ClosureState.ZERO_CARDS_NO_POT) {
            potManager.handlePot(false);
            return PutCombinationResult.success(PutCombinationResult.Status.SUCCESS_TAKE_POT, processedCombo, isPlayer1);
        }
        if (state == ClosureState.CAN_CLOSE) {
            closureManager.handleStateAfterAction(current);
            return PutCombinationResult.success(PutCombinationResult.Status.SUCCESS_CLOSE, processedCombo, isPlayer1);
        }
        if (state == ClosureState.ZERO_CARDS_NO_BURRACO) {
            closureManager.handleStateAfterAction(current);
            return PutCombinationResult.success(PutCombinationResult.Status.SUCCESS_STUCK, processedCombo, isPlayer1);
        }

        return PutCombinationResult.success(PutCombinationResult.Status.SUCCESS, processedCombo, isPlayer1);
    }
}
