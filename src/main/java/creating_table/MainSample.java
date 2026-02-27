package creating_table;

import model.player.*;

public class MainSample {
    public static void main(final String[] args){
        final Player player1 = new PlayerImpl();
        final Player player2 = new PlayerImpl();

        
        TableModel model = new TableModelImpl(player1, player2);

        TableView view = new TableViewImpl();

        TableController controller = new TableControllerImpl(model,view);
        controller.start();
    }

    
}
