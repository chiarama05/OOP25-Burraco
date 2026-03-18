package core.buttonLogic;

import model.player.Player;
import model.card.Card;
import core.combination.CombinationValidator;
import core.combination.StraightUtils;
import core.combination.SetUtils;
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

