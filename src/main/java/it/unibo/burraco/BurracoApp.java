package it.unibo.burraco;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.distribution.DistributionManagerImpl;
import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.controller.game.GameLoopController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.round.ResetManagerImpl;
import it.unibo.burraco.controller.round.RoundControllerImpl;
import it.unibo.burraco.controller.round.RoundEndHandler;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.GameModelImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.model.score.ScoreImpl;
import it.unibo.burraco.view.BurracoView;
import it.unibo.burraco.view.components.sound.SoundView;
import it.unibo.burraco.view.components.sound.SoundViewImpl;
import it.unibo.burraco.view.scenes.SetUpMenuView;
import it.unibo.burraco.view.scenes.OnConfigurationCompleteListener;
import it.unibo.burraco.view.scenes.ScoreViewImpl;
import it.unibo.burraco.view.scenes.SetUpMenuViewImpl;
import it.unibo.burraco.view.scenes.StartMenuView;
import it.unibo.burraco.view.scenes.StartMenuViewImpl;
import it.unibo.burraco.view.table.TableSetUpView;
import it.unibo.burraco.view.table.TableViewImpl;
import it.unibo.burraco.view.table.pot.PotNotifierImpl;

/**
 * Main application class and composition root.
 * Handles navigation between menus and wires all components together
 * when a game session starts.
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
        final TableSetUpView distributionView = tableView.getInitDist();

        final GameModel model = new GameModelImpl(nameP1, nameP2);
        final Player p1 = model.getPlayer1();
        final Player p2 = model.getPlayer2();

        final InitialDistributionController distribution =
                new InitialDistributionController(new DistributionManagerImpl());
        distribution.distribute(p1, p2, model.getCommonDeck(), model.getDiscardPile());

        distributionView.refresh(
                p1.getHand(),
                p2.getHand(),
                tableView.getDiscardView(),
                model.getDiscardPile().getCards());
        tableView.refreshHandPanel(true, p1.getHand());

        final PotNotifierImpl potNotifier = new PotNotifierImpl(tableView.getFrame());
        final PotManager potManager = new PotManager(model.getTurn(), tableView, potNotifier);

        final Score score = new ScoreImpl();

        final RoundEndHandler roundEndHandler = new RoundEndHandler(
                score,
                p1, p2,
                nameP1, nameP2,
                tableView,
                sound,
                targetScore,
                (playerA, playerB, n1, n2, target, s, tv, over) ->
                        new ScoreViewImpl(playerA, playerB, n1, n2, target, s, tv, over));

        final ScoreController scoreController = new ScoreController(
                score, p1, p2, roundEndHandler);

        final BurracoView burracoView = tableView;
        final GameLoopController loop = new GameLoopController(
                model,
                burracoView,
                sound,
                potManager,
                scoreController);

        final RoundControllerImpl roundCtrl = new RoundControllerImpl(
                tableView,
                new ResetManagerImpl(),
                p1, p2,
                model,
                new InitialDistributionController(new DistributionManagerImpl()),
                distributionView);

        scoreController.setOnNewRound(() -> {
            roundCtrl.processNewRound();
            loop.start();
        });
        loop.start();
    }
}
