package core.selectioncard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import core.controller.GameController;
import model.card.*;
import model.player.Player;
import view.table.TableViewImpl;

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

    /**
     * Tenta di calare le carte selezionate sul tavolo.
     */
    public void processCombination(Player player, TableViewImpl view, GameController controller) {
        if (selectedCards.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Seleziona prima delle carte!");
        return; 
    }

        
        List<Card> cardsToPut = new ArrayList<>(selectedCards);

        
        if (core.combination.CombinationValidator.isValidCombination(cardsToPut)) {
        
        // 1. Rimuovi dalla mano (Logica)
        for (Card c : cardsToPut) {
            player.removeCardHand(c);
        }
        
        // 2. Aggiungi al giocatore (Logica)
        player.getCombinations().add(cardsToPut);

        // 3. AGGIORNA IL TAVOLO (Grafica)
        // Questo metodo deve aggiungere un AttachedButton ai pannelli verdi
        view.addCombinationToPlayerPanel(cardsToPut, controller.isPlayer1(player));
        
        // 4. AGGIORNA LA MANO (Grafica)
        // Fondamentale per far sparire le carte selezionate dalla vista
        view.refreshHandPanel(player);

        // 5. Pulisci selezione
        clearSelection();
        
        System.out.println("Combinazione calata con successo!");
    } else {
        JOptionPane.showMessageDialog(null, "Combinazione non valida ai fini del regolamento!");
    }
}
}
