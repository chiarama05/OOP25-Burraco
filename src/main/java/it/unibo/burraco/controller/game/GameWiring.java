package it.unibo.burraco.controller.game; 

import it.unibo.burraco.controller.distribution.DistributionManagerImpl;
import it.unibo.burraco.controller.distribution.InitialDistributionController;
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
import it.unibo.burraco.view.scenes.InitialDistributionView;
import it.unibo.burraco.view.scenes.ScoreViewImpl;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.table.pot.PotNotifierImpl;

public final class GameWiring {

    public GameWiring(
            final String nameP1,
            final String nameP2,
            final TableView view,
            final SoundView soundView,
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

        // RoundEndHandler si occupa di suono, ScoreView e fine partita
        final RoundEndHandler roundEndHandler = new RoundEndHandler(
                score,
                p1, p2,
                nameP1, nameP2,
                view,
                soundView,
                targetScore,
                (playerA, playerB, n1, n2, target, s, tv, over) ->
                        new ScoreViewImpl(playerA, playerB, n1, n2, target, s, tv, over));

        // ScoreController ora è snello: calcola e delega
        final ScoreController scoreController = new ScoreController(
                score, p1, p2, roundEndHandler);

        scoreController.setRoundFactory(new RoundControllerImpl(
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
                soundView,
                potManager,
                scoreController);

        loop.start();
    }
}