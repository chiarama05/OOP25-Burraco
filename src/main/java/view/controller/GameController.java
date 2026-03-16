package view.controller;

import model.player.Player;
import model.player.PlayerImpl;
import core.selectioncard.SelectionCardManager;
import model.deck.DeckImpl;
import model.discard.DiscardPile;
import model.discard.DiscardPileImpl;
import model.turn.Turn;
import view.sound.SoundController;
import view.sound.SoundControllerImpl;


public class GameController {
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final DeckImpl commonDeck;
    private final Turn turnModel;
    private final SoundController soundController;
    private final DiscardPile discardPile;
    private boolean hasDrawn = false;
    private final SelectionCardManager selectionManager = new SelectionCardManager();
    private final core.drawcard.DrawManager drawManager = new core.drawcard.DrawManager();

    public GameController(PlayerImpl p1, PlayerImpl p2, Turn turnModel) {
        this.player1 = p1;
        this.player2 = p2;
        this.turnModel = turnModel;
        this.discardPile = new DiscardPileImpl();
        this.commonDeck = new DeckImpl();
        this.soundController = new SoundControllerImpl();
    }


    public boolean isPlayer1(Player p) {
        return p == player1;
    }

    public Player getCurrentPlayer() {
        return turnModel.getCurrentPlayer();
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

    public core.drawcard.DrawManager getDrawManager() {
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