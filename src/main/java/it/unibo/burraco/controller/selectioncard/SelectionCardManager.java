package it.unibo.burraco.controller.selectioncard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.selection.SelectionNotifier;
import it.unibo.burraco.view.selection.SelectionView;

public class SelectionCardManager {

    private Set<Card> selectedCards = new HashSet<>();

    public void toggleSelection(Card card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
        } else {
            selectedCards.add(card);
        }
    }

    public boolean isSelected(Card card) {
        return selectedCards.contains(card);
    }

    public Set<Card> getSelectedCards() {
        return new HashSet<>(selectedCards);
    }

    public void clearSelection() {
        selectedCards.clear();
    }

    public int getSelectionSize() {
        return selectedCards.size();
    }

    public boolean isEmpty() {
        return selectedCards.isEmpty();
    }

    public void processCombination(Player player, SelectionView view, GameController controller, SelectionNotifier notifier) {
    if (selectedCards.isEmpty()) {
        notifier.notifySelectionError("EMPTY_SELECTION");
        return;
    }

    List<Card> cardsToPut = new ArrayList<>(selectedCards);

    if (CombinationValidator.isValidCombination(cardsToPut)) {
        player.removeCards(cardsToPut);
        player.addCombination(cardsToPut);

        view.addCombinationToPlayerPanel(cardsToPut, controller.isPlayer1(player));
        view.refreshHandPanel(player);

        clearSelection();
    } else {
        notifier.notifySelectionError("INVALID_COMBINATION"); 
    }
}
}