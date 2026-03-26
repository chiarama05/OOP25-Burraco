package it.unibo.burraco.controller.game;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.combination.PutCombinationController;
import it.unibo.burraco.controller.discardcard.DiscardController;
import it.unibo.burraco.controller.discardcard.DiscardManagerImpl;
import it.unibo.burraco.controller.discardcard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.model.score.ScoreImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.view.combination.PutCombinationButton;
import it.unibo.burraco.view.deck.DeckButton;
import it.unibo.burraco.view.discard.DiscardButton;
import it.unibo.burraco.view.discard.TakeDiscardButton;
import it.unibo.burraco.view.notification.deck.DeckNotifier;
import it.unibo.burraco.view.notification.deck.DeckNotifierImpl;
import it.unibo.burraco.view.notification.game.GameNotifier;
import it.unibo.burraco.view.notification.game.GameNotifierImpl;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifier;
import it.unibo.burraco.view.notification.putcombination.PutCombinationNotifierImpl;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifier;
import it.unibo.burraco.view.notification.takediscard.TakeDiscardNotifierImpl;
import it.unibo.burraco.view.discard.TakeDiscardView;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.distribution.InitialDistributionView;


public class GameWiring {

    private final GameController gameController;

    public GameWiring(PlayerImpl p1, PlayerImpl p2, String nameP1, String nameP2,
                  Turn turnModel, TableView view, SoundController soundController,
                  int targetScore, InitialDistributionView distributionView){

        GameNotifier notifier = new GameNotifierImpl(view.getFrame());

        this.gameController = new GameController(p1, p2, turnModel, soundController);
        DrawManager drawManager = gameController.getDrawManager();

        TurnController turnCtrl = new TurnController(turnModel, drawManager);

        PotManager potManager = new PotManager(turnModel, view);

        Score score = new ScoreImpl();
        ScoreController scoreController = new ScoreController(
        score, p1, p2, nameP1, nameP2,
        view, gameController, soundController, targetScore,
        distributionView); // usa il parametro

        ClosureManager closureManager = new ClosureManager(
            turnModel, notifier, targetScore, scoreController);

        DiscardController discardCoreLogic = new DiscardController(
            new DiscardManagerImpl(gameController.getDiscardPile()),
            turnCtrl,
            potManager,
            closureManager,
            drawManager,
            turnModel);

        AttachButtonFactory attachFactory = new AttachButtonFactory(
            view, gameController, closureManager, potManager, view.getFrame());
        view.setAttachButtonFactory(attachFactory);

        DiscardButton discardButton = new DiscardButton(view, view.getDiscardView(), notifier, discardCoreLogic);
        discardButton.setIsPlayer1(turnModel.isPlayer1Turn());

        PutCombinationController putCombinationCtrl = new PutCombinationController(
            gameController, drawManager, potManager, closureManager, turnModel);

        PutCombinationNotifier putNotifier = new PutCombinationNotifierImpl(view.getFrame());
        PutCombinationButton putCombinationLogic = new PutCombinationButton(
            view, gameController, putCombinationCtrl, putNotifier);

        view.getPutComboBtn().addActionListener(e -> putCombinationLogic.handlePutCombination());

        putCombinationLogic.setIsPlayer1(turnModel.isPlayer1Turn());


        DeckNotifier deckNotifier = new DeckNotifierImpl(view.getFrame());
        DeckButton deckButton = new DeckButton(view.getDeckView(), drawManager, view, gameController, deckNotifier);
        deckButton.setIsPlayer1(turnModel.isPlayer1Turn());



              TakeDiscardController takeDiscardCtrl = new TakeDiscardController(
            drawManager, turnModel, gameController.getDiscardPile());
        
    TakeDiscardNotifier takeDiscardNotifier = new TakeDiscardNotifierImpl(view.getFrame());

       new TakeDiscardButton(
    view.getTakeDiscardBtn(),
    takeDiscardCtrl,
    (TakeDiscardView) view,
    turnModel,
    gameController.getDiscardPile(),
    takeDiscardNotifier);


        turnCtrl.setOnTurnChangedListener(() -> {
    boolean isP1 = turnModel.isPlayer1Turn();
    Player current = turnModel.getCurrentPlayer();
    view.refreshTurnLabel(isP1);
    view.switchHand(isP1);
    view.refreshHandPanel(isP1, current.getHand());
    discardButton.setIsPlayer1(isP1);
    putCombinationLogic.setIsPlayer1(isP1); 
    deckButton.setIsPlayer1(isP1);
});


  
    }

    public GameController getGameController() {
        return gameController;
    }
}