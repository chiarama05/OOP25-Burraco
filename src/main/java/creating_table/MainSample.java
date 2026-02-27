package creating_table;

import model.player.*;

public class MainSample {
    Player player1 = new PlayerImpl();
    Player player2 = new PlayerImpl();

    TableModel model = new TableModelImpl(p1,p2);

    TableView view = new TableViewwImpl();

    TableController controller = new TableControllerImpl(model,view);
    controller.start();
    
}
