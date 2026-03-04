import javax.swing.SwingUtilities;

import creating_table.TableController;
import creating_table.TableControllerImpl;
import creating_table.TableModel;
import creating_table.TableModelImpl;
import model.player.*;
import view.table.TableView;
import view.table.TableViewImpl;

public class BurracoApp {
    public static void main(final String[] args){
        SwingUtilities.invokeLater(() -> {
            TableViewImpl table = new TableViewImpl();
            table.initGame();
        });
    }
}
