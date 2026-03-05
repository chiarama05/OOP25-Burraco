import javax.swing.SwingUtilities;

import creating_table.TableModelImpl;
import core.discardcard.DiscardManagerImpl;
import core.distributioncard.DistributionManagerImpl;
import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import model.deck.DeckImpl;
import model.discard.DiscardPileImpl;
import model.player.PlayerImpl;
import model.turn.TurnManagerImpl;
import view.table.TableViewImpl;

public class BurracoApp {
    public static void main(final String[] args){
        SwingUtilities.invokeLater(() -> {

            PlayerImpl p1 = new PlayerImpl();
            PlayerImpl p2 = new PlayerImpl();
            TableModelImpl model = new TableModelImpl(p1, p2);

            
            DeckImpl deck= new DeckImpl();
            DiscardPileImpl discardPile = new DiscardPileImpl();
            SelectionCardManager selectionManager=new SelectionCardManager();
            DrawManager drawManager = new DrawManager();
            DistributionManagerImpl distManager=new DistributionManagerImpl();
            DiscardManagerImpl discardManager = new DiscardManagerImpl(discardPile);

            TableViewImpl view = new TableViewImpl(p1,p2,deck,selectionManager,drawManager,distManager);

            TurnManagerImpl turnManager = new TurnManagerImpl(model, view,drawManager);

            view.wireControllers(turnManager,discardManager);
        });
    }
}
