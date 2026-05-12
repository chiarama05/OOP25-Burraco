package it.unibo.burraco.controller.game;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.round.ResetManagerImpl;
import it.unibo.burraco.controller.round.RoundControllerImpl;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.GameModelImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.model.score.ScoreImpl;
import it.unibo.burraco.view.BurracoView;
import it.unibo.burraco.view.score.ScoreViewImpl;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.pot.PotNotifierImpl;

public final class GameWiring {

    public GameWiring(
            final String nameP1,
            final String nameP2,
            final TableView view,
            final SoundController soundController,
            final int targetScore,
            final InitialDistributionView distributionView) {

        final GameModel model = new GameModelImpl(nameP1, nameP2);
        final Player p1 = model.getPlayer1();
        final Player p2 = model.getPlayer2();

        final InitialDistributionController distribution =
                new InitialDistributionController(new DistributionManagerImpl());

        distribution.distribute(p1, p2, model.getCommonDeck(), model.getDiscardPile());

        distributionView.refresh(
                p1.getHand(),
                p2.getHand(),
                view.getDiscardView(),
                model.getDiscardPile().getCards());
        view.refreshHandPanel(true, p1.getHand());

        final PotNotifierImpl potNotifier = new PotNotifierImpl(view.getFrame());
        final PotManager potManager = new PotManager(model.getTurn(), view, potNotifier);

        final Score score = new ScoreImpl();

        final ScoreController scoreController = new ScoreController(
        score,
        p1, p2,
        nameP1, nameP2,
        view,
        model,
        soundController,
        targetScore,
        distributionView,
        SwingUtilities::invokeLater,
        (playerA, playerB, n1, n2, target, s, tv, over) ->
                new ScoreViewImpl(playerA, playerB, n1, n2, target, s, tv, over));

        scoreController.setRoundFactory(() -> new RoundControllerImpl(
        view,
        new ResetManagerImpl(),
        p1, p2,
        model,
        new InitialDistributionController(new DistributionManagerImpl()),
        distributionView));

        final BurracoView burracoView = (BurracoView) view;

        final GameLoopController loop = new GameLoopController(
                model,
                burracoView,
                soundController,
                potManager,
                scoreController);

        loop.start();
    }
}