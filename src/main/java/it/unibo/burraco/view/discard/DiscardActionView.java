package it.unibo.burraco.view.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;
import java.util.Set;

public interface DiscardActionView {
    Set<Card> getSelectedCards(boolean isPlayer1);
    void onDiscardSuccess(Player player, List<Card> updatedPile, boolean isPlayer1);
    void onDiscardError(String errorCode);
}