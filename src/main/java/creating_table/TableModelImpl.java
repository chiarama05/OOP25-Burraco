package creating_table;
import model.player.Player;

public class TableModelImpl implements TableModel{

    public enum Winner {
        NONE, PLAYER1, PLAYER2;
    }

    private final Player player1;
    private final Player player2;
    private boolean isPlayer1Turn = true;
    private boolean gameFinished=false;
    private Winner winner = Winner.NONE;


    public TableModelImpl(final Player p1, final Player p2){
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

    /*turn management --> if the player is not in pot (Pozzetto) and has no cards, then they take the pozzetto  */
    public void switchTurn() {
        if (gameFinished){
            return;
        }

        isPlayer1Turn = !isPlayer1Turn;
    }

    public void mustTakePot(){
        if (gameFinished){
            return;
        }

        final Player p=getCurrentPlayer();
        if(p.hasFinishedCards() && !p.isInPot()){
            p.setInPot(true);
            System.out.println("Congratulation! " +p + "you have just took the pot! ");
        }
    }

    public void PotFly(){
        if(gameFinished){
            return;
        }
        final Player p = getCurrentPlayer();
        if(p.hasFinishedCards() && !p.isInPot()){
            p.setInPot(true);
            p.drawPot();
            System.out.println("So impressive! you took 'pozzetto al volo' !");
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
            return;
        }

        gameFinished=true;
        winner=isPlayer1Turn ? Winner.PLAYER1 : Winner.PLAYER2;
    }

}