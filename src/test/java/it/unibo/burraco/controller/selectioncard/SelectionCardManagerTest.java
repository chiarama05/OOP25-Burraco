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

class SelectionCardManagerTest {

    private SelectionCardManager selectionManager;
    private Card card1;
    private Card card2;

    @BeforeEach
    void init() {
        this.selectionManager = new SelectionCardManager();
        this.card1 = mock(Card.class);
        this.card2 = mock(Card.class);
    }

    @Test
    void testToggleSelectionAddsNewCard() {
        this.selectionManager.toggleSelection(this.card1);
        assertTrue(this.selectionManager.isSelected(this.card1), "Card should be selected");
        assertEquals(1, this.selectionManager.getSelectionSize(), "Selection size should be 1");
    }

    @Test
    void testToggleSelectionRemovesExistingCard() {
        this.selectionManager.toggleSelection(this.card1);
        this.selectionManager.toggleSelection(this.card1);
        assertFalse(this.selectionManager.isSelected(this.card1), "Card should be deselected");
        assertTrue(this.selectionManager.isEmpty(), "Selection should be empty");
    }

    @Test
    void testClearSelection() {
        this.selectionManager.toggleSelection(this.card1);
        this.selectionManager.toggleSelection(this.card2);
        this.selectionManager.clearSelection();
        assertEquals(0, this.selectionManager.getSelectionSize());
        assertTrue(this.selectionManager.isEmpty());
    }

    @Test
    void testProcessCombinationEmptySelection() {
        final SelectionNotifier notifier = mock(SelectionNotifier.class);
        final Player player = mock(Player.class);
        final SelectionView view = mock(SelectionView.class);

        this.selectionManager.processCombination(player, view, true, notifier);

        verify(notifier).notifySelectionError("EMPTY_SELECTION");
        verifyNoInteractions(player, view);
    }

    @Test
    void testProcessCombinationInvalid() {
        this.selectionManager.toggleSelection(this.card1);
        final SelectionNotifier notifier = mock(SelectionNotifier.class);

        this.selectionManager.processCombination(mock(Player.class), mock(SelectionView.class), true, notifier);

        verify(notifier).notifySelectionError("INVALID_COMBINATION");
    }

    @Test
    void testProcessCombinationSuccessFlow() {
        // Setup a valid combination (three 7s)
        final Card c1 = new CardImpl("♥", "7");
        final Card c2 = new CardImpl("♠", "7");
        final Card c3 = new CardImpl("♦", "7");
        this.selectionManager.toggleSelection(c1);
        this.selectionManager.toggleSelection(c2);
        this.selectionManager.toggleSelection(c3);

        final Player player = mock(Player.class);
        final SelectionView view = mock(SelectionView.class);
        final SelectionNotifier notifier = mock(SelectionNotifier.class);

        this.selectionManager.processCombination(player, view, true, notifier);

        verify(player).removeCards(anyList());
        verify(player).addCombination(anyList());

        verify(view).addCombinationToPlayerPanel(anyList(), eq(true));
        verify(view).refreshHandPanel(eq(true), any());
        assertTrue(this.selectionManager.isEmpty());
    }
}
