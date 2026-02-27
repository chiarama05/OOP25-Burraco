

import creating_table.TableController;
import creating_table.TableControllerImpl;
import creating_table.TableModel;
import creating_table.TableModelImpl;
import model.player.*;
import view.table.TableView;
import view.table.TableViewImpl;

public class BurracoApp {
    public static void main(final String[] args){
        final Player player1 = new PlayerImpl();
        final Player player2 = new PlayerImpl();

        
        TableModel model = new TableModelImpl(player1, player2);

        TableView view = new TableViewImpl();

        TableController controller = new TableControllerImpl(model,view);
        controller.start();
    }

    
}
