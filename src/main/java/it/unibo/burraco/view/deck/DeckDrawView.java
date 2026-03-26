package it.unibo.burraco.view.deck;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

public interface DeckDrawView {
    void onDrawSuccess(Player current, List<Card> hand);
    void showDrawError(String message);
}
