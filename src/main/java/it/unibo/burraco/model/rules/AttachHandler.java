package it.unibo.burraco.model.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.CardValue;

/**
 * Utility class responsible for determining whether one or more cards
 * can be attached to an existing combination on the table.
 */
public final class AttachHandler {

    private final SetHandler setHandler;
    private final CombinationValidator combinationValidator;
    private final StraightUtils straightUtils;

    /**
     * Constructs a new AttachHandler and initializes its sub-validators.
     */
    public AttachHandler() {
        this.setHandler = new SetHandler();
        this.combinationValidator = new CombinationValidator();
        this.straightUtils = new StraightUtils();
    }

    /**
     * Checks if a list of new cards can be legally added to an existing combination.
     *
     * @param combination the current combination of cards on the table
     * @param newCards the list of cards to attempt to attach
     * @return true if the resulting combination is valid, false otherwise
     */
    public boolean canAttach(final List<Card> combination, final List<Card> newCards) {
        if (combination == null || combination.isEmpty()) {
            return false;
        }

        final List<Card> realCards = combination.stream()
            .filter(c -> !c.getValue().isJolly() && c.getValue() != CardValue.TWO)
            .collect(Collectors.toList());
            
        final boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(Collectors.toSet()).size() < realCards.size();

        final List<Card> hypothetical = new ArrayList<>(combination);
        hypothetical.addAll(newCards);

        if (hasDuplicateValues || setHandler.isValid(combination)
            || this.straightUtils.isSameSeed(combination)) {
            return this.combinationValidator.isValidCombination(hypothetical);
        }
        return false;
    }

    /**
     * Overloaded method to check if a single card can be attached to a combination.
     *
     * @param combination the existing combination
     * @param newCard the single card to attach
     * @return true if the card can be attached, false otherwise
     */
    public boolean canAttach(final List<Card> combination, final Card newCard) {
        return this.canAttach(combination, List.of(newCard));
    }
}
