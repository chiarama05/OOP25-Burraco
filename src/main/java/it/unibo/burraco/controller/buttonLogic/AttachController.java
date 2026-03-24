package it.unibo.burraco.controller.buttonLogic;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.controller.combination.AttachUtils;
import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.combination.StraightUtils;
import it.unibo.burraco.controller.closure.ClosureState;
import it.unibo.burraco.controller.closure.ClosureValidator;

import java.util.ArrayList;
import java.util.List;

public class AttachController {

    public AttachController() {}

    
    public AttachResult tryAttach(Player currentPlayer,
                                   List<Card> selectedCards,
                                   List<Card> combinationCards,
                                   boolean hasDrawn,
                                   boolean isCurrentPlayer) {

        
        if (!hasDrawn) {
            return AttachResult.NOT_DRAWN;
        }
        if (!isCurrentPlayer) {
            return AttachResult.WRONG_PLAYER;
        }
        if (selectedCards.isEmpty()) {
            return AttachResult.NO_CARDS_SELECTED;
        }

        List<Card> hypothetical = new ArrayList<>(combinationCards);
        hypothetical.addAll(selectedCards);

        if (StraightUtils.isSameSeed(combinationCards)) {
            hypothetical = StraightUtils.orderStraight(hypothetical);
        } else {
            hypothetical.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));
        }

        if (!CombinationValidator.isValidCombination(hypothetical)) {
            return AttachResult.INVALID_COMBINATION;
        }

        // Controllo preventivo "stuck"
        if (ClosureValidator.wouldGetStuckAfterAttach(currentPlayer, selectedCards, combinationCards.size())) {
            return AttachResult.WOULD_GET_STUCK;
        }


        
        int sizeBefore = combinationCards.size();
        boolean success = executeAttach(currentPlayer, selectedCards, combinationCards);

       if (!success) {
            return AttachResult.ATTACH_FAILED;
        }

        // Segnala al chiamante se è stato raggiunto un burraco (il suono lo suona la View/GameController)
        if (sizeBefore < 7 && combinationCards.size() >= 7) {
            return AttachResult.SUCCESS_BURRACO;
        }
        
       ClosureState state = ClosureValidator.evaluate(currentPlayer);

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

    
    private boolean executeAttach(Player player, List<Card> selectedCards, List<Card> combinationCards) {
        for (Card c : selectedCards) {
            if (!AttachUtils.canAttach(combinationCards, c)) {
                return false;
            }
        }

        combinationCards.addAll(selectedCards);

        for (List<Card> playerComb : player.getCombinations()) {
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
