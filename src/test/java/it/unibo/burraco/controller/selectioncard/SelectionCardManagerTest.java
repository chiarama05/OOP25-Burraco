package it.unibo.burraco.controller.selectioncard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.model.card.Card;

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
}
