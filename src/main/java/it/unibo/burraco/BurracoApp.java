package it.unibo.burraco;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.distribution.DistributionManagerImpl;
import it.unibo.burraco.controller.distribution.InitialDistributionController;
import it.unibo.burraco.controller.game.GameController;
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
import it.unibo.burraco.view.components.sound.SoundView;
import it.unibo.burraco.view.components.sound.SoundViewImpl;
import it.unibo.burraco.view.scenes.OnConfigurationCompleteListener;
import it.unibo.burraco.view.scenes.ScoreViewImpl;
import it.unibo.burraco.view.scenes.SetUpMenuView;
import it.unibo.burraco.view.scenes.SetUpMenuViewImpl;
import it.unibo.burraco.view.scenes.StartMenuView;
import it.unibo.burraco.view.scenes.StartMenuViewImpl;
import it.unibo.burraco.view.table.BurracoView;
import it.unibo.burraco.view.table.DistributionView;
import it.unibo.burraco.view.table.SwingTableAccess;
import it.unibo.burraco.view.table.TableViewImpl;

/**
 * Main application class and composition root.
 * Handles navigation between menus and wires all components together
 * when a game session starts.
 */
public final class BurracoApp {

    private BurracoApp() { }

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

    /**
     * Wires all components and starts a new game session.
     *
     * @param nameP1      display name for player 1
     * @param nameP2      display name for player 2
     * @param targetScore the score threshold required to win the match
     */
    private static void startGame(final String nameP1,
                                   final String nameP2,
                                   final int targetScore) {

        final SoundView sound = new SoundViewImpl();

        final TableViewImpl tableView = new TableViewImpl(nameP1, nameP2);
        final DistributionView distributionView = tableView.getInitDist();

        final GameModel model = new GameModelImpl(nameP1, nameP2);
        final Player p1 = model.getPlayer1();
        final Player p2 = model.getPlayer2();

        final InitialDistributionController distribution =
                new InitialDistributionController(new DistributionManagerImpl());
        distribution.distribute(p1, p2, model.getCommonDeck(), model.getDiscardPile());

        distributionView.refresh(p1.getHand(), p2.getHand());
        tableView.updateDiscardPile(model.getDiscardPile().getCards());
        tableView.refreshHandPanel(true, p1.getHand());

        final BurracoView burracoView = tableView;
        final SwingTableAccess swingAccess = tableView;

        final PotManager potManager = new PotManager(model.getTurn(), burracoView);

        final Score score = new ScoreImpl();

        final RoundEndHandler roundEndHandler = new RoundEndHandler(
                score, p1, p2, nameP1, nameP2,
                burracoView, swingAccess, sound,
                targetScore,                  
                (snap1, snap2, target, sa, over) ->
                        new ScoreViewImpl(snap1, snap2, target, sa, over));

        final ScoreController scoreController =
                new ScoreController(score, p1, p2, roundEndHandler);

        final GameController loop = new GameLoopController(
                model, burracoView, sound, potManager, scoreController);

        final RoundControllerImpl roundCtrl = new RoundControllerImpl(
                burracoView,
                new ResetManagerImpl(),
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