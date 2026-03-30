package it.unibo.burraco;
import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.game.GameWiring;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.TurnImpl;
import it.unibo.burraco.view.start.OnConfigurationCompleteListener;
import it.unibo.burraco.view.start.SetUpMenuView;
import it.unibo.burraco.view.start.SetUpMenuViewImpl;
import it.unibo.burraco.view.start.StartMenuView;
import it.unibo.burraco.view.start.StartMenuViewImpl;
import it.unibo.burraco.view.table.TableViewImpl;


public class BurracoApp {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> showStartMenu());
    }

    private static void showStartMenu() {
        StartMenuView startMenu = new StartMenuViewImpl(() -> showSetupMenu());
        startMenu.display();
    }

    private static void showSetupMenu() {
        OnConfigurationCompleteListener listener = new OnConfigurationCompleteListener() {
            
            @Override
            public void onConfigComplete(int targetScore, String nameP1, String nameP2) {
                
                PlayerImpl p1 = new PlayerImpl(nameP1);
                PlayerImpl p2 = new PlayerImpl(nameP2);
                TurnImpl turnManager = new TurnImpl(p1, p2);

                it.unibo.burraco.controller.sound.SoundController sound = new it.unibo.burraco.view.sound.SoundControllerImpl();
                
                SelectionCardManager selectionManager = new SelectionCardManager();
                TableViewImpl view = new TableViewImpl(nameP1, nameP2, selectionManager);

                GameWiring wiring = new GameWiring(p1, p2, nameP1, nameP2, turnManager, view, sound, targetScore,view.getInitDist());
                GameController gc = wiring.getGameController();

                InitialDistributionController distController =
                new InitialDistributionController(new DistributionManagerImpl());
                distController.distribute(p1, p2, gc.getCommonDeck(), gc.getDiscardPile());

                view.getInitDist().refresh(
                p1.getHand(),
                p2.getHand(),
                view.getDiscardView(),
                gc.getDiscardPile().getCards()
                );
                
                view.refreshHandPanel(true, p1.getHand());
            }

            @Override
            public void onBackClicked() {
                showStartMenu();
            }
        };

        SetUpMenuView setupMenu = new SetUpMenuViewImpl(listener);
        setupMenu.display();
    }
}