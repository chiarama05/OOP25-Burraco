package it.unibo.burraco.model.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.burraco.model.cards.Card;

public final class Move {

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

    public Move(final Type type, final List<Card> selectedCards) {
        this.type = type;
        this.selectedCards = new ArrayList<>(selectedCards);
        this.targetCombination = Collections.emptyList();
    }

    public Move(final Type type, final List<Card> selected, final List<Card> target) {
        this.type = type;
        this.selectedCards = new ArrayList<>(selected);
        this.targetCombination = new ArrayList<>(target);
    }

    public Type getType() { return type; }
    public List<Card> getSelectedCards() { return Collections.unmodifiableList(selectedCards); }
    public List<Card> getTargetCombination() { return Collections.unmodifiableList(targetCombination); }
}
