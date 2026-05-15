package it.unibo.burraco.view.table;

import it.unibo.burraco.view.table.deck.DeckView;
import it.unibo.burraco.view.table.discard.DiscardView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Aggregate view interface used only during wiring.
 * Combines GameView, PotView and TakeDiscardView so that
 * GameWiring can pass a single view object to all components
 * without depending on a concrete class.
 */
public interface TableView extends BurracoView, GameView {
 
    /**
     * @return the main application {@link JFrame}
     */
    JFrame getFrame();
 
    /**
     * @return the "Put Combination" {@link JButton}
     */
    JButton getPutComboBtn();
 
    /**
     * @return the "Take Discard" {@link JButton}
     */
    JButton getTakeDiscardBtn();
 
    /**
     * @return the {@link DeckView} component
     */
    DeckView getDeckView();
 
    /**
     * @return the {@link DiscardView} component
     */
    DiscardView getDiscardView();
 
    /**
     * @return the panel containing the discard pile labels
     */
    JPanel getDiscardPanel();
 
    /**
     * @return {@code true} if the currently active player is Player 1
     */
    boolean isCurrentPlayer1();
}