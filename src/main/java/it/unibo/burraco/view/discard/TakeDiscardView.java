package it.unibo.burraco.view.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

public interface TakeDiscardView {
    void refreshHandPanel(Player p);
    void updateDiscardPile(List<Card> cards);
}