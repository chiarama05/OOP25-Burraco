package it.unibo.burraco.view.notification.game;

public interface GameNotifier {

    void notifyPotTakenNextTurn();

    void notifyPotTakenFly();

    void notifyInvalidClosure();

    void notifyVictory(final String winnerName);

    void notifyMustDraw();

    void notifySelectionError(final String message);

    void notifyMustTakePotBeforeDiscard();
    
    void notifyMustFormBurracoBeforeClose();
}
