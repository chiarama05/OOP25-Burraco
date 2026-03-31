package it.unibo.burraco.controller.selectioncard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.selection.SelectionNotifier;
import it.unibo.burraco.view.selection.SelectionView;

/**
 * Manages the state of cards selected by the user in the UI.
 * Acts as a specialized buffer that tracks which cards are currently "active"
 * for potential moves like discarding or creating combinations.
 */
public class SelectionCardManager {

    /** Internal storage for selected cards, ensuring uniqueness via HashSet. */
    private final Set<Card> selectedCards = new HashSet<>();

    /**
     * Adds a card to the selection if not present, or removes it if already selected.
     * 
     * @param card the card to toggle.
     */
    public void toggleSelection(final Card card) {
        if (this.selectedCards.contains(card)) {
            this.selectedCards.remove(card);
        } else {
            this.selectedCards.add(card);
        }
    }

    /** 
     * @return true if the specified card is currently in the selection set. 
     */
    public boolean isSelected(final Card card) {
        return this.selectedCards.contains(card);
    }

    /**
     * Returns an unmodifiable view of the selected cards to prevent 
     * external corruption of the internal state.
     * 
     * @return a read-only Set of selected cards.
     */
    public Set<Card> getSelectedCards() {
        return Collections.unmodifiableSet(this.selectedCards);
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

    /**
     * Validates the current selection and attempts to play it as a new combination.
     * If valid, updates the player model and synchronizes the view.
     * 
     * @param player   the player attempting the move.
     * @param view     the view component to update on success.
     * @param isPlayer1 boolean flag to identify which UI panel to update.
     * @param notifier component to handle user feedback/errors.
     */
    public void processCombination(final Player player, final SelectionView view, 
                                   final boolean isPlayer1, final SelectionNotifier notifier) {

        if (this.selectedCards.isEmpty()) {
            notifier.notifySelectionError("EMPTY_SELECTION");
            return;
        }

        // Convert set to list for validation and processing
        final List<Card> cardsToPut = new ArrayList<>(selectedCards);

        // Delegation to specialized validator
        if (CombinationValidator.isValidCombination(cardsToPut)) {
            this.executeMove(player, view, isPlayer1, cardsToPut);
        } else {
            notifier.notifySelectionError("INVALID_COMBINATION");
        }
    }

    /**
     * Internal helper to execute the move once validated.
     * Updates models and refreshes the UI.
     */
    private void executeMove(final Player player, final SelectionView view, 
                             final boolean isPlayer1, final List<Card> cardsToPut) {
        player.removeCards(cardsToPut);
        player.addCombination(cardsToPut);

        view.addCombinationToPlayerPanel(cardsToPut, isPlayer1);
        view.refreshHandPanel(isPlayer1, player.getHand());

        this.clearSelection();
    }
}
