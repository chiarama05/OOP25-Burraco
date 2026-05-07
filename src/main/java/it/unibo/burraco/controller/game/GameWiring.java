package it.unibo.burraco.controller.game;

import javax.swing.SwingUtilities;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.combination.putcombination.PutCombinationActionController;
import it.unibo.burraco.controller.combination.putcombination.PutCombinationController;
import it.unibo.burraco.controller.deck.DeckActionController;
import it.unibo.burraco.controller.discardcard.discard.DiscardActionController;
import it.unibo.burraco.controller.discardcard.discard.DiscardController;
import it.unibo.burraco.controller.discardcard.discard.DiscardManagerImpl;
import it.unibo.burraco.controller.discardcard.takediscard.TakeDiscardActionController;
import it.unibo.burraco.controller.discardcard.takediscard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.GameModelImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.model.score.ScoreImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.combination.PutCombinationButton;
import it.unibo.burraco.view.deck.DeckButton;
import it.unibo.burraco.view.discardcard.discard.DiscardButton;
import it.unibo.burraco.view.discardcard.takediscard.TakeDiscardButton;
import it.unibo.burraco.view.discardcard.takediscard.TakeDiscardView;
import it.unibo.burraco.view.notification.closure.ClosureNotifier;
import it.unibo.burraco.view.notification.closure.ClosureNotifierImpl;
import it.unibo.burraco.view.notification.deck.DeckNotifier;
import it.unibo.burraco.view.notification.deck.DeckNotifierImpl;
import it.unibo.burraco.view.notification.discard.DiscardNotifierImpl;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifier;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifierImpl;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifier;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifierImpl;
import it.unibo.burraco.view.score.ScoreViewImpl;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;

/**
 * Wires the game components together.
 */
public final class GameWiring {

    private final GameController gameController;

    /**
     * Constructs and wires the entire game logic.
     *
     * @param nameP1            name of player 1
     * @param nameP2            name of player 2
     * @param view              the main table view
     * @param soundController   the sound controller
     * @param targetScore       points needed to win the match
     * @param distributionView  the initial distribution view
     */
    public GameWiring(
            final String nameP1, final String nameP2,
            final TableView view, final SoundController soundController,
            final int targetScore, final InitialDistributionView distributionView) {

        final GameModel gameModel = new GameModelImpl(nameP1, nameP2);
        final ClosureNotifier notifier = new ClosureNotifierImpl(view.getFrame());

        this.gameController = new GameController(gameModel, soundController);

        final Turn turnModel = gameModel.getTurn();
        final Player p1 = gameModel.getPlayer1();
        final Player p2 = gameModel.getPlayer2();
        final DrawManager drawManager = this.gameController.getDrawManager();

        final TurnController turnCtrl = new TurnController(turnModel, drawManager);
        final PotManager potManager = new PotManager(turnModel, view);

        final Score score = new ScoreImpl();
        final ScoreController.ViewProvider realViewProvider = (playerA, playerB, n1, n2, target, s, tv, over) ->
                new ScoreViewImpl(playerA, playerB, n1, n2, target, s, tv, over);

        final ScoreController scoreController = new ScoreController(
                score, p1, p2, nameP1, nameP2,
                view, this.gameController, soundController, targetScore,
                distributionView, SwingUtilities::invokeLater,
                realViewProvider);

        final ClosureManager closureManager = new ClosureManager(
                turnModel, notifier, targetScore, scoreController);

        final DiscardController discardCoreLogic = new DiscardController(
                new DiscardManagerImpl(gameModel.getDiscardPile()),
                turnCtrl, potManager, closureManager, drawManager, turnModel);

        final AttachButtonFactory attachFactory = new AttachButtonFactory(
                view, this.gameController, closureManager, potManager, view.getFrame());
        view.setAttachButtonFactory(attachFactory);

        final DiscardActionController discardActionCtrl = new DiscardActionController(discardCoreLogic);

        final DiscardNotifierImpl discardNotifier = new DiscardNotifierImpl(view.getFrame());
        final DiscardButton discardButton = new DiscardButton(view, view.getDiscardView(), discardNotifier);

        discardButton.setOnDiscardAction(discardActionCtrl::handle);

        final PutCombinationController putCombinationCtrl = new PutCombinationController(
                this.gameController, drawManager, potManager, closureManager);

        final PutCombinationNotifier putNotifier = new PutCombinationNotifierImpl(view.getFrame());
        final PutCombinationActionController putActionCtrl = new PutCombinationActionController(
                this.gameController, putCombinationCtrl, putNotifier);

        final PutCombinationButton putCombinationView = new PutCombinationButton(view);
        putCombinationView.setIsPlayer1(turnModel.isPlayer1Turn());

        putCombinationView.setOnPutCombination(() -> {
                putActionCtrl.handle(putCombinationView.getSelectedCards(), putCombinationView);
        });

        final DeckNotifier deckNotifier = new DeckNotifierImpl(view.getFrame());
        final DeckActionController deckActionCtrl = new DeckActionController(
                this.gameController, drawManager, deckNotifier);

        final DeckButton deckButton = new DeckButton(view.getDeckView(), view);
        deckButton.setIsPlayer1(turnModel.isPlayer1Turn());
        deckButton.setOnDrawAction(() -> deckActionCtrl.handle(deckButton));

        final TakeDiscardController takeDiscardCtrl = new TakeDiscardController(
                drawManager, turnModel, gameModel.getDiscardPile());

        final TakeDiscardActionController takeDiscardActionCtrl = new TakeDiscardActionController(
                takeDiscardCtrl, turnModel, this.gameController.getDiscardPile());

        final TakeDiscardNotifier takeDiscardNotifier = new TakeDiscardNotifierImpl(view.getFrame());

        final TakeDiscardButton takeDiscardButton = new TakeDiscardButton(
                view.getTakeDiscardBtn(), (TakeDiscardView) view, takeDiscardNotifier);

        takeDiscardButton.setOnTakeDiscardAction(() ->
                takeDiscardActionCtrl.handle(takeDiscardButton)
        );

        turnCtrl.setOnTurnChangedListener(() -> {
            final boolean isP1 = turnModel.isPlayer1Turn();
            final Player current = turnModel.getCurrentPlayer();

            view.refreshTurnLabel(isP1);
            view.switchHand(isP1);
            view.refreshHandPanel(isP1, current.getHand());
            discardButton.setIsPlayer1(isP1);
            putCombinationView.setIsPlayer1(isP1);
            deckButton.setIsPlayer1(isP1);
        });
    }

    /**
     * Returns the game controller instance.
     *
     * @return the game controller
     */
    public GameController getGameController() {
        return this.gameController;
    }
}
