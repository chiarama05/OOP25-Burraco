package it.unibo.burraco.view.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

public interface TakeDiscardActionView {
    void onTakeDiscardSuccess(Player current, List<Card> updatedPile);
    void onTakeDiscardError(String errorCode);
}