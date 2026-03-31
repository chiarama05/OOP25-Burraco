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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.List;

/**
 * Main view interface for the Burraco game table.
 * This interface acts as an aggregate contract for all table-level UI operations,
 * combining TakeDiscardView, PotView, and SelectionView.
 */
public interface TableView extends TakeDiscardView, PotView, SelectionView {

    /**
     * Updates the turn label to reflect the player whose turn it currently is.
     *
     * @param isP1 true to show Player 1's name, false for Player 2's
     */
    void refreshTurnLabel(boolean isP1);

    /**
     * Marks on the UI that a player has collected their pot (pozzetto).
     *
     * @param isP1 true if Player 1 took the pot, false for Player 2
     */
    void markPotTaken(boolean isP1);

    /**
     * Adds a newly placed combination to the specified player's combination panel.
     *
     * @param cards the cards forming the combination to display
     * @param isP1  true to add to Player 1's panel, false for Player 2's
     */
    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);

    /**
     * Handles the visual handover between players for privacy.
     *
     * @param isP1 true if the player now taking their turn is Player 1
     */
    void switchHand(boolean isP1);

    /**
     * Resets all panels in preparation for a new round.
     */
    void startNewRound();

    /**
     * Displays a modal dialog with the score summary.
     *
     * @param title   the title of the dialog window
     * @param message the formatted score message to display
     */
    void showScoreModal(String title, String message);

    /**
     * Forces a full repaint of the table components.
     */
    void repaintTable();

    /**
     * Returns the HandView associated with Player 1.
     *
     * @return Player 1's hand view
     */
    HandView getPlayer1HandView();

    /**
     * Returns the HandView associated with Player 2.
     *
     * @return Player 2's hand view
     */
    HandView getPlayer2HandView();

    /**
     * Returns the HandView for the player whose turn it currently is.
     *
     * @param isPlayer1 true to return Player 1's view, false for Player 2's
     * @return the hand view for the current player
     */
    HandView getHandViewForCurrentPlayer(boolean isPlayer1);

    /**
     * Refreshes the hand panel with the current list of cards.
     *
     * @param isPlayer1 true if refreshing Player 1's hand
     * @param hand      the list of cards to display
     */
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);

    /**
     * Returns the DiscardView component.
     *
     * @return the discard view
     */
    DiscardView getDiscardView();

    /**
     * Returns the panel containing the discard pile.
     *
     * @return the discard JPanel 
     */
    JPanel getDiscardPanel();

    /**
     * Returns the main application frame.
     *
     * @return the main JFrame 
     */
    JFrame getFrame();   
    
    /**
     * Returns the button used to place combinations.
     *
     * @return the put combination JButton 
     */
    JButton getPutComboBtn();      
    
    /** 
     * Returns the button used to take the discard pile.
     *
     * @return the take discard JButton 
     */
    JButton getTakeDiscardBtn();
    
    /**
     * Returns the DeckView component.
     *
     * @return the deck view 
     */
    DeckView getDeckView();    
    
    /**
     * Injects the factory for creating combination buttons.
     *
     * @param factory the factory instance to use
     */
    void setAttachButtonFactory(AttachButtonFactory factory);

    /**
     * Injects the manager for card selection.
     *
     * @param manager the selection manager instance to use
     */
    void setSelectionCardManager(SelectionCardManager manager);
}
