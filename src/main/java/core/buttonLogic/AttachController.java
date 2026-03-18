package core.buttonLogic;
    
    import model.card.Card;
    import model.player.Player;
    import core.combination.AttachUtils;
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

