package it.unibo.burraco.model.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.burraco.model.cards.Card;

/**
 * Represents a move attempted by a player during their turn.
 * It is an immutable object that encapsulates the type of action, the cards 
 * involved from the player's hand, and any target combination on the table.
 */
public final class Move {

    /**
     * Defines the possible actions a player can perform.
     */
    public enum Type {
        DRAW_DECK,
        DRAW_DISCARD,
        PUT_COMBINATION,
        ATTACH,
        DISCARD
    }

    private final Type type;
    private final List<Card> selectedCards;
    private final List<Card> targetCombination;

    /**
     * Constructs a move that doesn't require a target combination.
     * Used typically for drawing or discarding actions.
     *
     * @param type          the type of action
     * @param selectedCards the cards involved in the action
     */
    public Move(final Type type, final List<Card> selectedCards) {
        this.type = type;
        this.selectedCards = new ArrayList<>(selectedCards);
        this.targetCombination = Collections.emptyList();
    }

    /**
     * Constructs a move that involves a target combination on the table.
     * Used typically for attaching cards to existing melds.
     *
     * @param type     the type of action
     * @param selected the cards to be moved from the hand
     * @param target   the existing combination on the table to modify
     */
    public Move(final Type type, final List<Card> selected, final List<Card> target) {
        this.type = type;
        this.selectedCards = new ArrayList<>(selected);
        this.targetCombination = new ArrayList<>(target);
    }

    /** 
     * Returns the type of this move.
     * 
     * @return the move type
     */
    public Type getType() { 
        return this.type; 
    }

    /** 
     * Returns an unmodifiable view of the cards selected for this move.
     * 
     * @return an unmodifiable list of selected cards
     */
    public List<Card> getSelectedCards() { 
        return Collections.unmodifiableList(this.selectedCards); 
    }

    /**
     * Returns an unmodifiable view of the target combination, if any.
     * 
     * @return an unmodifiable list of the target combination cards
     */
    public List<Card> getTargetCombination() { 
        return Collections.unmodifiableList(this.targetCombination); 
    }
}
