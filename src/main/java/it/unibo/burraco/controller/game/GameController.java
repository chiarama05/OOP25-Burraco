package it.unibo.burraco.controller.game;

import it.unibo.burraco.model.player.Player;

import java.util.List;

import it.unibo.burraco.controller.attach.AttachController;
import it.unibo.burraco.controller.attach.AttachResult;
import it.unibo.burraco.controller.distributioncard.DistributionManagerImpl;
import it.unibo.burraco.controller.distributioncard.InitialDistributionController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.card.Card;

/**
 * Controller that manages the main game logic and state transitions.
 */
public final class GameController {

    private final GameModel model;
    private final SoundController soundController;
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
    public GameController(final GameModel model, final SoundController sc) {
        this.model = model;
        this.soundController = sc;
        this.attachController = new AttachController();
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

        final Player currentPlayer = model.getCurrentPlayer();
        final boolean drawingStatus = drawManager.hasDrawn();
        final boolean isCurrentPlayer = model.isPlayer1(currentPlayer) == isPlayer1Owner;

        final AttachResult result = this.attachController.tryAttach(
                currentPlayer, selectedCards, combinationCards, drawingStatus, isCurrentPlayer);

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
     * @return the AttachController
     */
    public AttachController getAttachController() {
        return this.attachController;
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

    public DiscardPile getDiscardPile() {
        return this.model.getDiscardPile();
    }

    /**
     * Returns whether the player is currently allowed to draw.
     *
     * @return true if the player has not yet drawn in this turn
     */
    public boolean canDraw() {
        return !this.drawManager.hasDrawn();
    }

    /**
     * Sets the legacy draw flag.
     *
     * @param drawnValue true to mark as drawn
     */
    public void setHasDrawn(final boolean drawnValue) {
        this.drawManager.setHasDrawn(drawnValue);
    }

    /**
     * Returns true if the player has already drawn in this turn.
     * 
     * @return true if already drawn
     */
    public boolean hasAlreadyDrawn() {
        return this.drawManager.hasDrawn();
    }

    public GameModel getModel() {
        return this.model;
    }
}
