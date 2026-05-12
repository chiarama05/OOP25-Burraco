package it.unibo.burraco;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.game.GameWiring;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.controller.sound.SoundControllerImpl;
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

    private BurracoApp() { }

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
            public void onConfigComplete(final int targetScore,
                                         final String nameP1,
                                         final String nameP2) {
                startGame(nameP1, nameP2, targetScore);
            }

            @Override
            public void onBackClicked() {
                showStartMenu();
            }
        };

        final SetUpMenuView setupMenu = new SetUpMenuViewImpl(listener);
        setupMenu.display();
    }

    private static void startGame(final String nameP1,
                                   final String nameP2,
                                   final int targetScore) {
        final SoundController sound = new SoundControllerImpl();
        final TableViewImpl tableView = new TableViewImpl(nameP1, nameP2);

        new GameWiring(nameP1, nameP2, tableView, sound, targetScore,
                tableView.getInitDist());
    }
}