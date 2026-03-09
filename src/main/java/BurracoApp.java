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
import view.start.SetUpMenuView;
import view.start.SetUpMenuViewImpl;
import view.start.StartMenuView;
import view.start.StartMenuViewImpl;
import view.table.TableViewImpl;

public class BurracoApp {
    public static void main(final String[] args){

        SwingUtilities.invokeLater(() -> {
            StartMenuView startMenu = new StartMenuViewImpl(() -> {
                SetUpMenuView setupMenu = new SetUpMenuViewImpl((scoreThreshold, nameP1, nameP2) -> {
                
            System.out.println("Game started with limit: " + scoreThreshold);
            PlayerImpl p1 = new PlayerImpl(nameP1);
            PlayerImpl p2 = new PlayerImpl(nameP2);
            TableModelImpl model = new TableModelImpl(p1, p2);

            
            DeckImpl deck= new DeckImpl();
            DiscardPileImpl discardPile = new DiscardPileImpl();
            SelectionCardManager selectionManager=new SelectionCardManager();
            DrawManager drawManager = new DrawManager();
            DistributionManagerImpl distManager=new DistributionManagerImpl();
            DiscardManagerImpl discardManager = new DiscardManagerImpl(discardPile);

            TableViewImpl view = new TableViewImpl(p1,p2,deck,selectionManager,drawManager,distManager,nameP1,nameP2);

            TurnManagerImpl turnManager = new TurnManagerImpl(model, view,drawManager);

            view.wireControllers(turnManager,discardManager);
        });
        setupMenu.display();
    });
    startMenu.display();
});
}
}

