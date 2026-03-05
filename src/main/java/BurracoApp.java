import javax.swing.SwingUtilities;

import creating_table.TableModelImpl;
import model.player.PlayerImpl;
import model.turn.TurnManagerImpl;
import view.table.TableViewImpl;

public class BurracoApp {
    public static void main(final String[] args){
        SwingUtilities.invokeLater(() -> {

            PlayerImpl p1 = new PlayerImpl();
            PlayerImpl p2 = new PlayerImpl();
            TableModelImpl model = new TableModelImpl(p1, p2);

            
            TableViewImpl view = new TableViewImpl();

            TurnManagerImpl turnManager = new TurnManagerImpl(model, view);

            view.refreshTurnLabel(model.isPlayer1Turn());
        });
    }
}
