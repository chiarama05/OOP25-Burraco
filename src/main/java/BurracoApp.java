import javax.swing.SwingUtilities;

import creating_table.TableController;
import creating_table.TableControllerImpl;
import creating_table.TableModelImpl;
import creating_table.TableViewImpl;
import model.player.Player;
import model.player.PlayerImpl;
import creating_table.*;

public class BurracoApp {

    public static void main(String[] args) {

        // Creation player
        Player player1 = new PlayerImpl();
        Player player2 = new PlayerImpl();

        // Creation MVC
        TableModel model = new TableModelImpl(player1, player2);
        TableView view = new TableViewImpl();
        TableController controller = new TableControllerImpl(model, view);

        // Start application
        controller.start();
    }  
}
