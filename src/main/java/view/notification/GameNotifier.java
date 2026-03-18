package view.notification;

public interface GameNotifier {

    void notifyPotTakenNextTurn();

    void notifyPotTakenFly();

    void notifyInvalidClosure();

    void notifyVictory(String winnerName);

    void notifyMustDraw();

    void notifySelectionError(String message);

    void notifyMustTakePotBeforeDiscard();
    
    void notifyMustFormBurracoBeforeClose();

}
