package it.unibo.burraco.controller.selectioncard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.burraco.model.card.Card;

/**
 * Manages the state of cards selected by the user in the UI.
 * Acts as a specialized buffer that tracks which cards are currently "active"
 * for potential moves like discarding or creating combinations.
 */
public class SelectionCardManager {

    private final List<Card> selectedCards = new ArrayList<>();

    /**
     * Adds a card to the selection if not present, or removes it if already selected.
     *
     * @param card the card to toggle.
     */
    public void toggleSelection(final Card card) {
        final boolean removed = this.selectedCards.removeIf(c ->
            System.identityHashCode(c) == System.identityHashCode(card));
        if (!removed) {
            this.selectedCards.add(card);
        }
    }

    /**
     * @param card the card to check.
     * @return true if the specified card is currently in the selection set.
     */
    public boolean isSelected(final Card card) {
        return this.selectedCards.stream().anyMatch(c ->
            System.identityHashCode(c) == System.identityHashCode(card));
    }

    /**
     * Returns an unmodifiable view of the selected cards.
     *
     * @return a read-only List of selected cards.
     */
    public List<Card> getSelectedCards() {
        return Collections.unmodifiableList(selectedCards);
    }

    /** Clears all currently selected cards. */
    public void clearSelection() {
        this.selectedCards.clear();
    }

    /**
     * @return the total number of currently selected cards.
     */
    public int getSelectionSize() {
        return this.selectedCards.size();
    }

    /**
     * @return true if the selection buffer is empty.
     */
    public boolean isEmpty() {
        return this.selectedCards.isEmpty();
    }
}
