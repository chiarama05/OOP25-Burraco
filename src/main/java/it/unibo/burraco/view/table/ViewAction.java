package it.unibo.burraco.view.table;

import it.unibo.burraco.model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable DTO representing a player's intended action.
 * Built by the View, consumed by the Controller, which translates it into a
 * {@link it.unibo.burraco.model.move.Move}.
 *
 * The View may still reference {@link Card} (a model entity) because cards
 * are the fundamental data being displayed — but it never references
 * {@code Move} or {@code Move.Type}.
 */
public final class ViewAction {

    private final PlayerAction type;
    private final List<Card> selectedCards;
    private final List<Card> targetCombination;

    /**
     * Constructs a ViewAction without a target combination
     * (draw, discard, put combination).
     *
     * @param type          the player's intended action
     * @param selectedCards the cards selected from the hand
     */
    public ViewAction(final PlayerAction type, final List<Card> selectedCards) {
        this.type = type;
        this.selectedCards = new ArrayList<>(selectedCards);
        this.targetCombination = Collections.emptyList();
    }

    /**
     * Constructs a ViewAction with a target combination (attach).
     *
     * @param type              the player's intended action
     * @param selectedCards     the cards selected from the hand
     * @param targetCombination the existing combination to attach to
     */
    public ViewAction(final PlayerAction type,
                      final List<Card> selectedCards,
                      final List<Card> targetCombination) {
        this.type = type;
        this.selectedCards = new ArrayList<>(selectedCards);
        this.targetCombination = new ArrayList<>(targetCombination);
    }

    /**
     * Returns the type of action the player intends to perform.
     *
     * @return the {@link PlayerAction} representing the intended action
     */
    public PlayerAction getType() {
        return type;
    }

    /**
     * Returns the cards selected from the player's hand for this action.
     *
     * @return an unmodifiable view of the selected cards
     */
    public List<Card> getSelectedCards() {
        return Collections.unmodifiableList(selectedCards);
    }

    /**
     * Returns the target combination this action is directed at, if any.
     * For actions that do not involve an existing combination (draw, discard,
     * put combination), this list is empty.
     *
     * @return an unmodifiable view of the target combination, or an empty list
     */
    public List<Card> getTargetCombination() {
        return Collections.unmodifiableList(targetCombination);
    }
}
