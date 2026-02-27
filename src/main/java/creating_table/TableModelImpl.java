package creating_table;

import java.util.*;

import model.card.*;
import model.player.*;

public class TableModelImpl implements TableModel{

    public enum Winner {
        NONE, PLAYER1, PLAYER2;
    }

    private final Player player1;
    private final Player player2;


    private boolean isPlayer1Turn = true;
    private boolean haspicked = false;
    private boolean gameFinished=false;
    private Winner winner = Winner.NONE;



    /*public TableModelImpl(){
        this(PlayerImpl());
    }*/

    public TableModelImpl(Player p1, Player p2){
        this.player1=p1;
        this.player2=p2;
    }


    //GETTER
    public Player getPlayer1() {
        return player1; 
    }

    public Player getPlayer2() {
        return player2; 
    }

    public Player getCurrentPlayer() {
        return isPlayer1Turn ? player1 : player2;
    }

    public Player getOpponent() {
        return isPlayer1Turn ? player2 : player1;
    }

    public boolean isPlayer1Turn() { 
        return isPlayer1Turn;
    }

    public boolean isGameFinished() {
        return gameFinished; 
    }

    /*public Winner getWinner() {
        return winner; 
    }*/


    /*turn management --> if the player is not in pot (Pozzetto) and has no cards, then they take the pozzetto  */
    public void switchTurn() {
        if (gameFinished){
            return;
        }

        isPlayer1Turn = !isPlayer1Turn;

        Player newCurrent = getCurrentPlayer();

        if (newCurrent.isInPot() && newCurrent.hasFinishedCards()) {
            //newCurrent.takePozzetto(); //da creare
        }
    }

    public void mustTakePot(){
        if (gameFinished){
            return;
        }

        Player p=getCurrentPlayer();
        if(p.hasFinishedCards() && !p.isInPot()){
            p.setInPot(true);
            //p.takePozzetto();
        }
    }

    public void PotFly(){
        if(gameFinished){
            return;
        }

        Player p=getCurrentPlayer();

        if(p.hasFinishedCards() && !p.isInPot()){
            p.setInPot(true);
            mustTakePot();
        }
    }



    //CLOSURE AND VICTORY MANAGEMENT

    public boolean canClose(){
        if(gameFinished){
            return false;
        }

        return getCurrentPlayer().getBurracoCount()>=1;
    }


    public void confirmClosureAndWin(){
        if(gameFinished){
            return;
        }
        if(!canClose()){
            return; //"player : " + getCurrentPlayer() " can't close";
        }

        gameFinished=true;
        winner=isPlayer1Turn ? Winner.PLAYER1 : Winner.PLAYER2;
    }

}

   