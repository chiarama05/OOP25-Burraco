package it.unibo.burraco.view.notification.game;

/**
 * Interface responsible for notifying the user about game events and rules.
 */
public interface GameNotifier {

    /**
     * Notifies that the pot was taken and can be used in the next turn.
     */
    void notifyPotTakenNextTurn();

    /**
     * Notifies that the pot was taken 'on the fly' and can be used immediately.
     */
    void notifyPotTakenFly();

    /**
     * Notifies that the closure attempt was invalid.
     */
    void notifyInvalidClosure();

    /**
     * Notifies the victory of a player.
     *
     * @param winnerName the name of the winner.
     */
    void notifyVictory(final String winnerName);

    /**
     * Notifies that a draw action is required before discarding.
     */
    void notifyMustDraw();

    /**
     * Notifies a generic selection error.
     *
     * @param message the error message to display.
     */
    void notifySelectionError(final String message);

    /**
     * Notifies that the pot was taken automatically before a discard.
     */
    void notifyMustTakePotBeforeDiscard();

    /**
     * Notifies that a Burraco is required to close the round.
     */
    void notifyMustFormBurracoBeforeClose();
}
