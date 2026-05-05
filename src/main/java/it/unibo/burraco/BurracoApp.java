package it.unibo.burraco;

import javax.swing.SwingUtilities;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.game.GameWiring;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.GameModel;
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
        SwingUtilities.invokeLater(BurracoApp::showStartMenu);
    }

    private static void showStartMenu() {
        final StartMenuView startMenu = new StartMenuViewImpl(BurracoApp::showSetupMenu);
        startMenu.display();
    }

    private static void showSetupMenu() {
        final OnConfigurationCompleteListener listener = new OnConfigurationCompleteListener() {

            @Override
            public void onConfigComplete(final int targetScore, final String nameP1, final String nameP2) {

                final it.unibo.burraco.controller.sound.SoundController sound = 
                new it.unibo.burraco.view.sound.SoundControllerImpl();

                final SelectionCardManager selectionManager = new SelectionCardManager();
                final TableViewImpl view = new TableViewImpl(nameP1, nameP2, selectionManager);

                final GameWiring wiring = new GameWiring(nameP1, nameP2, 
                    view, sound, targetScore, view.getInitDist());
                final GameController gc = wiring.getGameController();
                final GameModel model = gc.getModel();

                gc.getDistributionController().distribute(
                    model.getPlayer1(), 
                    model.getPlayer2(), 
                    model.getCommonDeck(), 
                    model.getDiscardPile()
                );

                view.getInitDist().refresh(
                    model.getPlayer1().getHand(),
                    model.getPlayer2().getHand(),
                    view.getDiscardView(),
                    model.getDiscardPile().getCards()
                );

                view.refreshHandPanel(true, model.getPlayer1().getHand());
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
