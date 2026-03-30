package it.unibo.burraco.view.start;

/**
 * Listener interface to handle the start of a new game session.
 */
@FunctionalInterface
public interface OnGameStartListener {
    
    /**
     * Triggered when the user clicks the "New Match" button.
     */
    void onStartClicked();
}
