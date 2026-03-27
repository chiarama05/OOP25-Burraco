package it.unibo.burraco.view.discardcard.takediscard;

import it.unibo.burraco.model.card.Card;
import java.util.List;

public interface TakeDiscardView {
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
    void updateDiscardPile(List<Card> cards);
}