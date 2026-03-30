package it.unibo.burraco.view.start;

/**
 * Listener interface for handling game configuration events.
 */
public interface OnConfigurationCompleteListener {

    /**
     * Called when the user confirms the configuration.
     * 
     * @param targetScore The score required to win the match.
     * @param name1 Name of the first player.
     * @param name2 Name of the second player.
     */
    void onConfigComplete(int targetScore, String name1, String name2);

    /**
     * Called when the user clicks the back button to return to the main menu.
     */
    void onBackClicked();
}
