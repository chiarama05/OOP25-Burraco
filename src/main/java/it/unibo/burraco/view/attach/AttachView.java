package it.unibo.burraco.view.attach;

import it.unibo.burraco.model.player.Player;

public interface AttachView {
    void showAttachError(String message, String title);
    void updateCombinationVisuals();
    void onAttachSuccess(Player p);
    void onAttachTakePot(Player p);
    void onAttachClose(Player p);
}
