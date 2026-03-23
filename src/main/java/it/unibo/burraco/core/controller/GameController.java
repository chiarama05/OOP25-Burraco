package it.unibo.burraco.core.controller;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.core.SoundController;
import it.unibo.burraco.core.buttonLogic.AttachController;
import it.unibo.burraco.core.buttonLogic.PutCombinationController;
import it.unibo.burraco.core.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.core.distributioncard.InitialDistributionController;
import it.unibo.burraco.core.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;
import it.unibo.burraco.model.turn.Turn;


public class GameController {
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final DeckImpl commonDeck;
    private final Turn turnModel;
    private final SoundController soundController;
    private final DiscardPile discardPile;
    private boolean hasDrawn = false;
    private final SelectionCardManager selectionManager = new SelectionCardManager();
    private final it.unibo.burraco.core.drawcard.DrawManager drawManager = new it.unibo.burraco.core.drawcard.DrawManager();
    private final AttachController attachController;
    private final PutCombinationController combinationController;
    private final InitialDistributionController distributionController;


    public GameController(PlayerImpl p1, PlayerImpl p2, Turn turnModel, SoundController sc) {
        this.player1 = p1;
        this.player2 = p2;
        this.turnModel = turnModel;
        this.discardPile = new DiscardPileImpl();
        this.commonDeck = new DeckImpl();
        this.attachController = new AttachController();
        this.soundController = sc;
        this.combinationController = new PutCombinationController();
        this.distributionController = new InitialDistributionController(new DistributionManagerImpl());
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

    public PutCombinationController getCombinationController() {
    return combinationController;
}

    public it.unibo.burraco.core.drawcard.DrawManager getDrawManager() {
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