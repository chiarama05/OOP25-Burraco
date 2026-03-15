package model.turn;


import model.player.Player;
import creating_table.TableModelImpl;
import view.table.TableViewImpl;
import core.drawcard.DrawManager;

public class TurnManagerImpl implements TurnManager{

    private final TableModelImpl model;
    private final TableViewImpl view;
    private final DrawManager drawManager;

    public TurnManagerImpl(final TableModelImpl model, final TableViewImpl view, final DrawManager drawManager){

        this.model=model;
        this.view=view;
        this.drawManager=drawManager;

        view.refreshTurnLabel(model.isPlayer1Turn());
        view.switchHand(model.isPlayer1Turn());
    }

    public Player getCurrentPlayer(){
        return model.getCurrentPlayer();
    }

    @Override
    public void nextTurn(){

        final Player prev= model.getCurrentPlayer();
        if(prev.isInPot() && prev.hasFinishedCards()){
            prev.drawPot();
        }

        model.switchTurn();
        if(this.drawManager!=null){
            this.drawManager.resetTurn();
        }
        
        view.refreshTurnLabel(model.isPlayer1Turn());
        view.switchHand(model.isPlayer1Turn());

        model.mustTakePot();
    }

    @Override
    public void resetForNewRound() {
    // 1. Forza il modello a tornare al giocatore 1
    if (!model.isPlayer1Turn()) {
        model.switchTurn(); 
    }
    
    // 2. Resetta il manager della pesca
    if (this.drawManager != null) {
        this.drawManager.resetTurn();
    }

    // 3. Aggiorna la grafica immediatamente
    view.refreshTurnLabel(true);
    view.refreshHandPanel(model.getCurrentPlayer());
    }

    @Override
    public void tryClosure(){
        if(!model.canClose()){
            view.showNotValideClosure();
            return;
        }
        model.confirmClosureAndWin();
        view.showWinExit(model.isPlayer1Turn());
    }
}
