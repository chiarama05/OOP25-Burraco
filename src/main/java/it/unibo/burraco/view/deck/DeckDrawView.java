package it.unibo.burraco.view.deck;

import it.unibo.burraco.model.player.Player;

public interface DeckDrawView {
    void onDrawSuccess(Player current);
    void showDrawError(String message);
}
