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

/** 
 * Main application class. 
 */
public final class BurracoApp {

    private BurracoApp() {
        // Utility class constructor
    }

    /**
     * Main entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> showStartMenu());
    }

    private static void showStartMenu() {
        final StartMenuView startMenu = new StartMenuViewImpl(() -> showSetupMenu());
        startMenu.display();
    }

    private static void showSetupMenu() {
        final OnConfigurationCompleteListener listener = new OnConfigurationCompleteListener() {

            @Override
            public void onConfigComplete(final int targetScore, final String nameP1, final String nameP2) {

                final PlayerImpl p1 = new PlayerImpl(nameP1);
                final PlayerImpl p2 = new PlayerImpl(nameP2);
                final TurnImpl turnManager = new TurnImpl(p1, p2);

                final it.unibo.burraco.controller.sound.SoundController sound = new it.unibo.burraco.view.sound.SoundControllerImpl();

                final SelectionCardManager selectionManager = new SelectionCardManager();
                final TableViewImpl view = new TableViewImpl(nameP1, nameP2, selectionManager);

                final GameWiring wiring = new GameWiring(p1, p2, nameP1, nameP2, 
                    turnManager, view, sound, targetScore,view.getInitDist());
                final GameController gc = wiring.getGameController();

                final InitialDistributionController distController =
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

        final SetUpMenuView setupMenu = new SetUpMenuViewImpl(listener);
        setupMenu.display();
    }
}
