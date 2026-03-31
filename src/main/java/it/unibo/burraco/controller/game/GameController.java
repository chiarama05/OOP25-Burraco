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
import it.unibo.burraco.model.card.Card;


/**
 * Controller that manages the main game logic and state transitions.
 */
public final class GameController {

    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final Deck commonDeck;
    private final Turn turnModel;
    private final SoundController soundController;
    private final DiscardPile discardPile;
    private boolean hasDrawn;
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
    public GameController(final PlayerImpl p1, final PlayerImpl p2, final Turn turnModel, final SoundController sc) {
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
     *
     * @param selectedCards    the cards the player wants to attach
     * @param combinationCards the existing combination to attach to
     * @param isPlayer1Owner   true if the combination belongs to Player 1
     * @return an AttachResult describing the outcome
     */
    public AttachResult tryAttach(final List<Card> selectedCards,
                                  final List<Card> combinationCards,
                                  final boolean isPlayer1Owner) {

        final Player currentPlayer = turnModel.getCurrentPlayer();
        final boolean hasDrawn = drawManager.hasDrawn();
        final boolean isCurrentPlayer = isPlayer1(currentPlayer) == isPlayer1Owner;

        final AttachResult result = this.attachController.tryAttach(
                currentPlayer, selectedCards, combinationCards, hasDrawn, isCurrentPlayer);

        if (result == AttachResult.SUCCESS_BURRACO) {
            this.soundController.playBurracoSound();
        }
        return result;
    }

    /**
     * Returns the initial distribution controller used during the setup phase.
     *
     * @return the InitialDistributionController
     */
    public InitialDistributionController getDistributionController() {
        return this.distributionController;
    }

    /**
     * Checks whether the given player is Player 1.
     *
     * @param p the player to test
     * @return true if p is the same object as Player 1
     */
    public boolean isPlayer1(final Player p) {
        return this.player1.equals(p);
    }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current Player
     */
    public Player getCurrentPlayer() {
        return this.turnModel.getCurrentPlayer();
    }

    /**
     * @return the AttachController
     */
    public AttachController getAttachController() {
        return this.attachController;
    }

    /**
     * @return the shared Deck
     */
    public Deck getCommonDeck() {
        return this.commonDeck;
    }

    /**
     * @return the SoundController
     */
    public SoundController getSoundController() {
        return this.soundController;
    }

    /**
     * @return the SelectionCardManager
     */
    public SelectionCardManager getSelectionManager() {
        return this.selectionManager;
    }

    /**
     * @return the DrawManager
     */
    public DrawManager getDrawManager() {
        return this.drawManager;
    }

    /**
     * @return the shared DiscardPile
     */
    public DiscardPile getDiscardPile() {
        return this.discardPile;
    }

    /**
     * @return the Turn model
     */
    public Turn getTurnModel() {
        return this.turnModel;
    }

    /**
     * Returns whether the player is currently allowed to draw.
     *
     * @return true if the player has not yet drawn in this turn
     */
    public boolean canDraw() {
        return !this.hasDrawn;
    }

    /**
     * Sets the legacy draw flag.
     *
     * @param value true to mark as drawn, false to allow drawing again
     */
    public void setHasDrawn(final boolean drawnValue) {
        this.hasDrawn = drawnValue;
    }


    /**
     * Returns true if the player has already drawn in this turn.
     *
     * @return true if already drawn
     */
    public boolean hasAlreadyDrawn() {
        return this.hasDrawn;
    }

    /**
     * @return the first player
     */
    public PlayerImpl getPlayer1() {
        return this.player1;
    }

    /**
     * @return the second player
     */
    public PlayerImpl getPlayer2() {
        return this.player2;
    }
}
