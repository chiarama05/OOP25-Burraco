package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.discardcard.discard.DiscardView;
import it.unibo.burraco.view.discardcard.takediscard.TakeDiscardView;
import it.unibo.burraco.view.pot.PotView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Aggregate view interface used only during wiring.
 * Combines GameView, PotView and TakeDiscardView so that
 * GameWiring can pass a single view object to all components
 * without depending on a concrete class.
 */
public interface TableView extends GameView, PotView, TakeDiscardView {

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
     * Injects the factory for creating combination buttons.
     *
     * @param factory the factory instance to use
     */
    void setAttachButtonFactory(AttachButtonFactory factory);
}