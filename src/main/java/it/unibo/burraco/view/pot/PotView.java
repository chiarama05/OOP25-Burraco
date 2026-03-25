package it.unibo.burraco.view.pot;

import it.unibo.burraco.model.player.Player;

public interface PotView {
    void showPotMessage(String playerName, boolean isDiscard);
    void markPotTaken(boolean isP1);
    void refreshHandPanel(Player p);
}
