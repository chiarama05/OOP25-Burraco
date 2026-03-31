package it.unibo.burraco.controller.game;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;

import java.util.List;

import it.unibo.burraco.controller.attach.AttachController;
import it.unibo.burraco.controller.attach.AttachResult;
import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.card.*;


/**
 * Central controller that owns and exposes the core domain objects of a Burraco match.
 * <p>
 * Acts as a service-locator / façade: it holds the two players, the shared deck,
 * the discard pile, the turn model, and the supporting managers
 * ({@link DrawManager}, {@link SelectionCardManager}, {@link AttachController}).
 * Higher-level controllers obtain references to these components through this class.
 * </p>
 */
public class GameController {
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final Deck commonDeck;
    private final Turn turnModel;
    private final SoundController soundController;
    private final DiscardPile discardPile;
    private boolean hasDrawn = false;
    private final SelectionCardManager selectionManager = new SelectionCardManager();
    private final DrawManager drawManager = new DrawManager();
    private final AttachController attachController;
    private final InitialDistributionController distributionController;


    /**
     * Constructs a GameController and initialises all core game components.
     *
     * @param p1        the first player
     * @param p2        the second player
     * @param turnModel the turn model tracking whose turn it is
     * @param sc        the sound controller for audio feedback
     */
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



    /**
     * Attempts to attach a set of selected cards to an existing combination.
     * Delegates the validation to {@link AttachController} and triggers the burraco sound
     * on a successful burraco.
     *
     * @param selectedCards    the cards the player wants to attach
     * @param combinationCards the existing combination to attach to
     * @param isPlayer1Owner   {@code true} if the combination belongs to Player 1
     * @return an {@link AttachResult} describing the outcome
     */
    public AttachResult tryAttach(List<Card> selectedCards,List<Card> combinationCards,boolean isPlayer1Owner) {

        Player currentPlayer = turnModel.getCurrentPlayer();
        boolean hasDrawn = drawManager.hasDrawn();
        boolean isCurrentPlayer = (isPlayer1(currentPlayer) == isPlayer1Owner);

        AttachResult result = attachController.tryAttach(currentPlayer,selectedCards,combinationCards,hasDrawn,isCurrentPlayer);

        
        if (result == AttachResult.SUCCESS_BURRACO) {
            soundController.playBurracoSound();
        }

        return result;
    }


     /**
     * Returns the initial distribution controller used during the setup phase.
     *
     * @return the {@link InitialDistributionController}
     */
    public InitialDistributionController getDistributionController() {
        return distributionController;
    }


    /**
     * Checks whether the given player is Player 1.
     *
     * @param p the player to test
     * @return {@code true} if {@code p} is the same object as Player 1
     */
    public boolean isPlayer1(Player p) {
        return p == player1;
    }


    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current {@link Player}
     */
    public Player getCurrentPlayer() {
        return turnModel.getCurrentPlayer();
    }

    /** @return the {@link AttachController} */
    public AttachController getAttachController() {
        return attachController;
    }

    /** @return the shared {@link Deck} */
    public Deck getCommonDeck() {
        return commonDeck;
    }

    /** @return the {@link SoundController} */
    public SoundController getSoundController() {
        return soundController;
    }

    /** @return the {@link SelectionCardManager} */
    public SelectionCardManager getSelectionManager() {
        return selectionManager;
    }

    /** @return the {@link DrawManager} */
    public DrawManager getDrawManager() {
        return drawManager;
    }

    /** @return the shared {@link DiscardPile} */
    public DiscardPile getDiscardPile() {
        return this.discardPile;
    }

    /** @return the {@link Turn} model */
    public Turn getTurnModel() {
        return this.turnModel;
    }

    /**
     * Returns whether the player is currently allowed to draw.
     * Note: this flag is separate from {@link DrawManager#hasDrawn()} and is used only
     * by legacy code paths; prefer {@link DrawManager} for new code.
     *
     * @return {@code true} if the player has not yet drawn in this turn
     */
    public boolean canDraw() { 
        return !hasDrawn; 
    }

    /**
     * Sets the legacy draw flag.
     *
     * @param value {@code true} to mark as drawn, {@code false} to allow drawing again
     */
    public void setHasDrawn(boolean value) { 
        this.hasDrawn = value; 
    }


    /**
     * Returns {@code true} if the player has already drawn in this turn (legacy flag).
     *
     * @return {@code true} if already drawn
     */
    public boolean hasAlreadyDrawn() { 
        return hasDrawn; 
    }

    /** @return the first player */
    public PlayerImpl getPlayer1() { 
        return player1; 
    }

    /** @return the second player */
    public PlayerImpl getPlayer2() { 
        return player2; 
    }
}
