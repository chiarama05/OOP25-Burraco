package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.discardcard.discard.DiscardView;
import it.unibo.burraco.view.discardcard.takediscard.TakeDiscardView;
import it.unibo.burraco.view.hand.HandView;
import it.unibo.burraco.view.pot.PotView;
import it.unibo.burraco.view.selection.SelectionView;

import javax.swing.*;
import java.util.List;


/**
 * Main view interface for the Burraco game table.
 * <p>
 * Extends {@link TakeDiscardView}, {@link PotView}, and {@link SelectionView}
 * to aggregate all table-level UI operations into a single contract.
 * Covers turn management, combination display, hand rendering, round transitions,
 * and access to sub-components such as the deck and discard views.
 * </p>
 */
public interface TableView extends TakeDiscardView, PotView, SelectionView {

    /**
     * Updates the turn label to reflect the player whose turn it currently is.
     *
     * @param isP1 {@code true} to show Player 1's name, {@code false} for Player 2's
     */
    void refreshTurnLabel(boolean isP1);

    void markPotTaken(boolean isP1);

    /**
     * Adds a newly placed combination to the specified player's combination panel.
     *
     * @param cards the cards forming the combination to display
     * @param isP1  {@code true} to add to Player 1's panel, {@code false} for Player 2's
     */
    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);

    /**
     * Handles the visual handover between players for privacy during a shared-screen game.
     * Hides the current player's cards and displays a dialog asking the next player to confirm
     * they are ready before the new hand is revealed.
     *
     * @param isP1 {@code true} if the player now taking their turn is Player 1
     */
    void switchHand(boolean isP1);

    /**
     * Resets all combination panels, the discard pile panel, and panel borders
     * in preparation for a new round.
     */
    void startNewRound();

    /**
     * Displays a modal dialog with the score summary at the end of a round or match.
     *
     * @param title   the title of the dialog window
     * @param message the formatted score message to display
     */
    void showScoreModal(String title, String message);

    /**
     * Forces a full repaint of the discard area and propagates the repaint
     * request up to the enclosing window.
     */
    void repaintTable();

    /**
     * Returns the {@link HandView} associated with Player 1.
     *
     * @return Player 1's hand view
     */
    HandView getPlayer1HandView();

    /**
     * Returns the {@link HandView} associated with Player 2.
     *
     * @return Player 2's hand view
     */
    HandView getPlayer2HandView();

    /**
     * Returns the {@link HandView} for the player whose turn it currently is.
     *
     * @param isPlayer1 {@code true} to return Player 1's view, {@code false} for Player 2's
     * @return the hand view for the current player
     */
    HandView getHandViewForCurrentPlayer(boolean isPlayer1);

    void refreshHandPanel(boolean isPlayer1, List<Card> hand);

    /**
     * Returns the {@link DiscardView} component responsible for rendering the discard pile.
     *
     * @return the discard view
     */
    DiscardView getDiscardView();

    /** @return the Swing panel that contains the discard pile UI component */
    JPanel getDiscardPanel();

    /** @return the main application {@link JFrame} */
    JFrame getFrame();   
    
    /** @return the "PUT COMBINATION" action button */
    JButton getPutComboBtn();      
    
    /** @return the "TAKE DISCARD" action button */
    JButton getTakeDiscardBtn();
    
    /** @return the {@link DeckView} component */
    DeckView getDeckView();    
    
    /**
     * Injects the {@link AttachButtonFactory} used to create combination buttons on the table.
     *
     * @param factory the factory instance to use
     */
    void setAttachButtonFactory(AttachButtonFactory factory);

    /**
     * Injects the {@link SelectionCardManager} that tracks which cards are currently selected.
     *
     * @param manager the selection manager instance to use
     */
    void setSelectionCardManager(SelectionCardManager manager);
}