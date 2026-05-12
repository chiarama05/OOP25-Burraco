package it.unibo.burraco.view.pot;

/**
 * Notifies the user about pot-related events.
 */
public interface PotNotifier {

    /**
     * Displays a message informing the player that they have taken the pot.
     *
     * @param playerName the name of the player who took the pot
     * @param isDiscard  true if the pot was triggered by a discard action,
     *                   false if taken on the fly
     */
    void notifyPotTaken(String playerName, boolean isDiscard);
}
