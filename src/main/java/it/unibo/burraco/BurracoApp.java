package it.unibo.burraco;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.game.GameWiring;
import it.unibo.burraco.view.components.sound.SoundView;
import it.unibo.burraco.view.components.sound.SoundViewImpl;
import it.unibo.burraco.view.scenes.OnConfigurationCompleteListener;
import it.unibo.burraco.view.scenes.SetUpMenuView;
import it.unibo.burraco.view.scenes.SetUpMenuViewImpl;
import it.unibo.burraco.view.scenes.StartMenuView;
import it.unibo.burraco.view.scenes.StartMenuViewImpl;
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
        final SoundView sound = new SoundViewImpl();
        final TableViewImpl tableView = new TableViewImpl(nameP1, nameP2);

        new GameWiring(nameP1, nameP2, tableView, sound, targetScore,
                tableView.getInitDist());
    }
}