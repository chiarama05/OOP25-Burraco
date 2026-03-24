package it.unibo.burraco.controller.game;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

import java.util.List;

import it.unibo.burraco.controller.SoundController;
import it.unibo.burraco.controller.buttonLogic.AttachController;
import it.unibo.burraco.controller.buttonLogic.AttachResult;
import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.card.*;


public class GameController {
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final DeckImpl commonDeck;
    private final Turn turnModel;
    private final SoundController soundController;
    private final DiscardPile discardPile;
    private boolean hasDrawn = false;
    private final SelectionCardManager selectionManager = new SelectionCardManager();
    private final it.unibo.burraco.controller.drawcard.DrawManager drawManager = new it.unibo.burraco.controller.drawcard.DrawManager();
    private final AttachController attachController;
    private final InitialDistributionController distributionController;


    public GameController(PlayerImpl p1, PlayerImpl p2, Turn turnModel, SoundController sc) {
        this.player1 = p1;
        this.player2 = p2;
        this.turnModel = turnModel;
        this.discardPile = new DiscardPileImpl();
        this.commonDeck = new DeckImpl();
        this.attachController = new AttachController();
        this.soundController = sc;
        this.distributionController = new InitialDistributionController(new DistributionManagerImpl());
}


public AttachResult tryAttach(List<Card> selectedCards,
                               List<Card> combinationCards,
                               boolean isPlayer1Owner) {

    Player currentPlayer = turnModel.getCurrentPlayer();
    boolean hasDrawn = drawManager.hasDrawn();
    boolean isCurrentPlayer = (isPlayer1(currentPlayer) == isPlayer1Owner);

    AttachResult result = attachController.tryAttach(
            currentPlayer,
            selectedCards,
            combinationCards,
            hasDrawn,
            isCurrentPlayer
    );

    // Il suono appartiene al controller, non alla View
    if (result == AttachResult.SUCCESS_BURRACO) {
        soundController.playBurracoSound();
    }

    return result;
}
public InitialDistributionController getDistributionController() {
    return distributionController;
}
    public boolean isPlayer1(Player p) {
        return p == player1;
    }

    public Player getCurrentPlayer() {
        return turnModel.getCurrentPlayer();
    }

    public AttachController getAttachController() {
    return attachController;
    }

    public DeckImpl getCommonDeck() {
        return commonDeck;
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public SelectionCardManager getSelectionManager() {
        return selectionManager;
    }

    public it.unibo.burraco.controller.drawcard.DrawManager getDrawManager() {
        return drawManager;
    }

    public DiscardPile getDiscardPile() {
        return this.discardPile;
    }

    public Turn getTurnModel() {
    return this.turnModel;
    }

    public boolean canDraw() { 
        return !hasDrawn; 
    }

    public void setHasDrawn(boolean value) { 
        this.hasDrawn = value; 
    }

    public boolean hasAlreadyDrawn() { 
        return hasDrawn; 
    }
    public PlayerImpl getPlayer1() { 
        return player1; 
    }
    public PlayerImpl getPlayer2() { 
        return player2; 
    }
}