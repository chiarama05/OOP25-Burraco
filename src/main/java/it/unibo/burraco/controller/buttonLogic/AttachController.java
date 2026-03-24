package it.unibo.burraco.controller.buttonLogic;
    
    import it.unibo.burraco.controller.combination.AttachUtils;
import it.unibo.burraco.model.card.Card;
    import it.unibo.burraco.model.player.Player;

import java.util.List;
    
    public class AttachController {
    
        public boolean executeAttach(Player player, List<Card> selectedCards, List<Card> combinationCards) {
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

