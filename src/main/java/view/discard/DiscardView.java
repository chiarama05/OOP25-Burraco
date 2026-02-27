package view.discard;

import java.util.List;
import model.card.*;
import java.awt.event.ActionListener;

/**
 * View interface responsible for displaying
 * the discard pile and handling discard actions.
 */
public interface DiscardView {

    /**
     * Updates the graphical representation of the discard pile.
     *
     * @param discardPile the list of cards currently in the discard pile
     */
    void updateDiscardPile(List<Card> discardPile);

    /**
     * Registers a listener for the discard button.
     *
     * @param listener the ActionListener triggered when the discard button is pressed
     */
    void setDiscardListener(ActionListener listener);
}
