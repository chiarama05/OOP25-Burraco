

import javax.swing.SwingUtilities;
import core.distributioncard.DistributionManagerImpl;
import model.player.PlayerImpl;
import model.turn.TurnImpl;
import view.start.SetUpMenuView;
import view.start.SetUpMenuViewImpl;
import view.start.StartMenuView;
import view.start.StartMenuViewImpl;
import view.table.TableViewImpl;
import view.controller.GameController;

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
                
                
                TableViewImpl view = new TableViewImpl(p1, p2, nameP1, nameP2);

                
                TurnImpl turnManager = new TurnImpl(p1, p2);

                view.setTargetScore(scoreThreshold);
                view.wireControllers(turnManager);
                
                
                GameController gc = view.getGameController(); 
                view.getInitDist().distribute(
                    p1, p2, 
                    new DistributionManagerImpl(), 
                    gc.getCommonDeck(), 
                    view.getDiscardView(), 
                    gc.getDiscardPile()
                );

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