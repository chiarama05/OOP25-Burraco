package it.unibo.burraco.controller.buttonLogic;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.combination.SetUtils;
import it.unibo.burraco.controller.combination.StraightUtils;
import it.unibo.burraco.model.card.Card;

import java.util.List;
import java.util.ArrayList;

public class PutCombinationController {

    public List<Card> tryPutCombination(Player player, List<Card> selected) {
        
        if (selected == null || selected.size() < 3) {
            return null; 
        }


        if (!CombinationValidator.isValidCombination(selected)) {
            return null;
        }

  
        List<Card> finalCombo = new ArrayList<>(selected);
        if (StraightUtils.isSameSeed(finalCombo) && !SetUtils.isValidSet(finalCombo)) {
            finalCombo = StraightUtils.orderStraight(finalCombo);
        }

        player.addCombination(finalCombo);
        player.removeCards(selected);

        return finalCombo; 
    }
}

