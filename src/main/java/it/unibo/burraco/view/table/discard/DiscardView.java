package it.unibo.burraco.view.table.discard;

import java.util.List;
import it.unibo.burraco.model.cards.Card;
import java.awt.event.ActionListener;

/**
 * View interface responsible for displaying the discard pile and handling discard actions.
 * It defines the contract for updating the cards on screen and capturing user interaction.
 */
public interface DiscardView {

    /**
     * Updates the graphical representation of the discard pile.
     *
     * @param discardPile the list of cards currently in the discard pile.
     */
    void updateDiscardPile(List<Card> discardPile);

    /**
     * Registers a listener for the discard button.
     * This decouples the view from the controller logic.
     *
     * @param listener the ActionListener triggered when the discard button is pressed.
     */
    void setDiscardListener(ActionListener listener);
}
