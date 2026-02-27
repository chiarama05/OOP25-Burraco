package core.selectioncard;

import java.util.HashSet;
import java.util.Set;
import model.card.*;

/**
 * SelectionCardManager handles the logical selection of cards..
 */
public class SelectionCardManager {

    /** 
     * Set containing currently selected cards 
     * */
    private Set<Card> selectedCards = new HashSet<>();

    /**
     * Toggles the selection state of a card.
     *
     * If the card is already selected, it will be removed.
     * If it is not selected, it will be added.
     *
     * @param card the card whose selection state must be toggled
     */
    public void toggleSelection(Card card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
        } else {
            selectedCards.add(card);
        }
    }

    /**
     * Checks whether a specific card is currently selected.
     *
     * @param card the card to check
     * @return true if the card is selected, false otherwise
     */
    public boolean isSelected(Card card) {
        return selectedCards.contains(card);
    }

    /**
     * Returns a defensive copy of the selected cards.
     *
     * A new Set is returned to preserve encapsulation and
     * prevent external modification of the internal state.
     *
     * @return a copy of the selected cards
     */
    public Set<Card> getSelectedCards() {
        return new HashSet<>(selectedCards);
    }

    /**
     * Clears all selected cards.
     */
    public void clearSelection() {
        selectedCards.clear();
    }

    /**
     * Returns the number of currently selected cards.
     *
     * @return the size of the selection set
     */
    public int getSelectionSize() {
        return selectedCards.size();
    }

    /**
     * Checks whether no cards are currently selected.
     *
     * @return true if the selection set is empty
     */
    public boolean isEmpty() {
        return selectedCards.isEmpty();
    }

}
