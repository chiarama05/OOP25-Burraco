package it.unibo.burraco;
import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.TurnImpl;
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
        SetUpMenuView.OnConfigurationCompleteListener listener = new SetUpMenuView.OnConfigurationCompleteListener() {
            
            @Override
            public void onConfigComplete(int scoreThreshold, String nameP1, String nameP2) {
                
                PlayerImpl p1 = new PlayerImpl(nameP1);
                PlayerImpl p2 = new PlayerImpl(nameP2);
                TurnImpl turnManager = new TurnImpl(p1, p2);

                it.unibo.burraco.controller.SoundController sound = new it.unibo.burraco.view.sound.SoundControllerImpl();
                
                TableViewImpl view = new TableViewImpl(p1, p2, nameP1, nameP2, sound);
                view.setTargetScore(scoreThreshold);
                view.wireControllers(turnManager);
                
                
            GameController gc = view.getGameController(); 

            InitialDistributionController distController = new InitialDistributionController(new DistributionManagerImpl());
            distController.distribute(p1, p2, gc.getCommonDeck(), gc.getDiscardPile());

            view.getInitDist().refresh(p1, p2, view.getDiscardView(), gc.getDiscardPile());

            view.refreshHandPanel(p1);
               
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