package it.unibo.burraco.controller.selectioncard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.card.CardImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.selection.SelectionNotifier;
import it.unibo.burraco.view.selection.SelectionView;

public class SelectionCardManagerTest {
    private SelectionCardManager selectionManager;
    private Card card1;
    private Card card2;

    @BeforeEach
    void init() {
        selectionManager = new SelectionCardManager();
        card1 = mock(Card.class);
        card2 = mock(Card.class);
    }

    @Test
    void testToggleSelectionAddsNewCard() {
        selectionManager.toggleSelection(card1);
        assertTrue(selectionManager.isSelected(card1));
        assertEquals(1, selectionManager.getSelectionSize());
    }

    @Test
    void testToggleSelectionRemovesExistingCard() {
        selectionManager.toggleSelection(card1);
        selectionManager.toggleSelection(card1); // Second toggle: remove
        assertFalse(selectionManager.isSelected(card1));
        assertTrue(selectionManager.isEmpty());
    }

    @Test
    void testClearSelection() {
        selectionManager.toggleSelection(card1);
        selectionManager.toggleSelection(card2);
        selectionManager.clearSelection();
        assertEquals(0, selectionManager.getSelectionSize());
        assertTrue(selectionManager.isEmpty());
    }

    @Test
    void testProcessCombinationEmptySelection() {
        SelectionNotifier notifier = mock(SelectionNotifier.class);
        Player player = mock(Player.class);
        SelectionView view = mock(SelectionView.class);

        selectionManager.processCombination(player, view, true, notifier);

        verify(notifier).notifySelectionError("EMPTY_SELECTION");
        verifyNoInteractions(player, view);
    }

    @Test
    void testProcessCombinationInvalid() {
        // Supponiamo che una sola carta non sia una combinazione valida (es. servono tris o scale)
        selectionManager.toggleSelection(card1);
        SelectionNotifier notifier = mock(SelectionNotifier.class);
        
        // Nota: Se CombinationValidator è statico, il test dipende dalla sua logica reale.
        // Se card1 non forma una combinazione valida:
        selectionManager.processCombination(mock(Player.class), mock(SelectionView.class), true, notifier);

        verify(notifier).notifySelectionError("INVALID_COMBINATION");
    }

    @Test
    void testProcessCombinationSuccessFlow() {
        // Simuliamo una combinazione valida (es. 3 carte uguali)
        Card c1 = new CardImpl("♥", "7");
        Card c2 = new CardImpl("♠", "7");
        Card c3 = new CardImpl("♦", "7");
        
        selectionManager.toggleSelection(c1);
        selectionManager.toggleSelection(c2);
        selectionManager.toggleSelection(c3);

        Player player = mock(Player.class);
        SelectionView view = mock(SelectionView.class);
        SelectionNotifier notifier = mock(SelectionNotifier.class);

        selectionManager.processCombination(player, view, true, notifier);

        // Verifichiamo l'aggiornamento del modello
        verify(player).removeCards(anyList());
        verify(player).addCombination(anyList());

        // Verifichiamo l'aggiornamento della vista
        verify(view).addCombinationToPlayerPanel(anyList(), eq(true));
        verify(view).refreshHandPanel(eq(true), any());

        // Verifichiamo che la selezione sia stata pulita dopo il successo
        assertTrue(selectionManager.isEmpty());
    }
}
