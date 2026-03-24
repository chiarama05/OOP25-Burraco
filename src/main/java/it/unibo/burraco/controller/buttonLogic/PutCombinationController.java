package it.unibo.burraco.controller.buttonLogic;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;
import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.buttonLogic.PutCombinationResult;
import it.unibo.burraco.controller.combination.SetUtils;
import it.unibo.burraco.controller.combination.StraightUtils;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

import java.util.ArrayList;
import java.util.List;

public class PutCombinationController {

    private final GameController gameController;
    private final DrawManager drawManager;
    private final PotManager potManager;
    private final ClosureManager closureManager;
    private final Turn turnModel;

    public PutCombinationController(GameController gameController,
                                    DrawManager drawManager,
                                    PotManager potManager,
                                    ClosureManager closureManager,
                                    Turn turnModel) {
        this.gameController = gameController;
        this.drawManager = drawManager;
        this.potManager = potManager;
        this.closureManager = closureManager;
        this.turnModel = turnModel;
    }

    public PutCombinationResult tryPutCombination(List<Card> selectedCards) {

        // 1. Pre-condizioni
        if (!drawManager.hasDrawn()) {
            return PutCombinationResult.error(PutCombinationResult.Status.NOT_DRAWN);
        }
        if (selectedCards.isEmpty()) {
            return PutCombinationResult.error(PutCombinationResult.Status.NO_CARDS_SELECTED);
        }

        Player current = turnModel.getCurrentPlayer();

        if (ClosureValidator.wouldGetStuckAfterPutCombo(current, selectedCards, selectedCards.size())) {
            return PutCombinationResult.error(PutCombinationResult.Status.WOULD_GET_STUCK);
        }

        
        if (selectedCards.size() < 3 || !CombinationValidator.isValidCombination(selectedCards)) {
            return PutCombinationResult.error(PutCombinationResult.Status.INVALID_COMBINATION);
        }

        
        List<Card> processedCombo = new ArrayList<>(selectedCards);
        if (StraightUtils.isSameSeed(processedCombo) && !SetUtils.isValidSet(processedCombo)) {
            processedCombo = StraightUtils.orderStraight(processedCombo);
        }


        current.addCombination(processedCombo);
        current.removeCards(selectedCards);


        if (processedCombo.size() >= 7) {
            gameController.getSoundController().playBurracoSound();
        }

        boolean isPlayer1 = gameController.isPlayer1(current);

        ClosureState state = ClosureValidator.evaluate(current);

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

