package it.unibo.burraco.view.table.hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.burraco.model.cards.Card;

/**
 * Manages the state of cards selected by the user in the UI.
 * Acts as a specialized buffer that tracks which cards are currently "active"
 * for potential moves like discarding or creating combinations.
 */
public final class SelectionCardManager {

    private final List<Card> selectedCards = new ArrayList<>();

    /**
     * Default constructor for SelectionCardManager.
     */
    public SelectionCardManager() {
        // Explicit constructor required by standard Checkstyle rules
    }

    /**
     * Adds a card to the selection if not present, or removes it if already selected.
     * Uses reference equality (==) to distinguish between cards with identical values.
     *
     * @param card the card to toggle
     */
    public void toggleSelection(final Card card) {
        final boolean removed = this.selectedCards.removeIf(c -> c == card);
        if (!removed) {
            this.selectedCards.add(card);
        }
    }

    /**
     * Checks whether a specific card is currently selected.
     *
     * @param card the card to check
     * @return true if the specified card instance is currently in the selection set
     */
    public boolean isSelected(final Card card) {
        return this.selectedCards.stream().anyMatch(c -> c == card);
    }

    /**
     * Returns an unmodifiable view of the selected cards.
     *
     * @return a read-only List of selected cards
     */
    public List<Card> getSelectedCards() {
        return Collections.unmodifiableList(selectedCards);
    }

    /**
     * Clears all currently selected cards.
     */
    public void clearSelection() {
        this.selectedCards.clear();
    }

    /**
     * Returns the total number of currently selected cards.
     *
     * @return the total number of currently selected cards
     */
    public int getSelectionSize() {
        return this.selectedCards.size();
    }

    /**
     * Checks if the selection buffer is empty.
     *
     * @return true if the selection buffer is empty
     */
    public boolean isEmpty() {
        return this.selectedCards.isEmpty();
    }
}
