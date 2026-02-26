package core.selectioncard;

import java.util.HashSet;
import java.util.Set;
import model.card.*;

/**
 * Manages the logical selection of cards.
 * This class contains ONLY business logic.
 */
public class SelectionCardManager {

    /** Set containing currently selected cards */
    private Set<Card> selectedCards = new HashSet<>();

    /**
     * Toggle the selection state of a card.
     * If the card is selected, it becomes unselected.
     * If it is not selected, it becomes selected.
     */
    public void toggleSelection(Card card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
        } else {
            selectedCards.add(card);
        }
    }

    /**
     * Returns true if the card is currently selected.
     */
    public boolean isSelected(Card card) {
        return selectedCards.contains(card);
    }

    /**
     * Returns a copy of the selected cards.
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
     * Returns the number of selected cards.
     */
    public int getSelectionSize() {
        return selectedCards.size();
    }

    /**
     * Returns true if no cards are selected.
     */
    public boolean isEmpty() {
        return selectedCards.isEmpty();
    }

}
