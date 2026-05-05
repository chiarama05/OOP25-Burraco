package it.unibo.burraco.controller.combination.set;

import java.util.List;
import java.util.stream.Collectors;
import it.unibo.burraco.model.card.Card;

public class SetHandler {

    private static final String JOLLY = "Jolly";
    private static final String TWO = "2";

    public SetHandler() { }

    public boolean isValid(final List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }

        final List<Card> naturalCards = cards.stream()
                .filter(c -> !isWildcard(c))
                .collect(Collectors.toList());

        if (naturalCards.isEmpty()) {
            return false;
        }

        long wildcardCount = cards.stream().filter(this::isWildcard).count();
        if (wildcardCount > 1) {
            return false;
        }

        final String baseValue = naturalCards.get(0).getValue();
        return naturalCards.stream().allMatch(c -> c.getValue().equals(baseValue));
    }

    public boolean canAttach(final List<Card> set, final Card newCard) {
        if (set == null || newCard == null) {
            return false;
        }

        final long wildcards = set.stream().filter(this::isWildcard).count();

        if (isWildcard(newCard)) {
            return wildcards < 1;
        }

        return set.stream()
                .filter(c -> !isWildcard(c))
                .findFirst()
                .map(c -> c.getValue().equals(newCard.getValue()))
                .orElse(true); 
    }

    /**
     * Internal helper to identify wildcards in a Set context.
     * In a Set, Jolly and 2 are always wildcards.
     */
    private boolean isWildcard(final Card c) {
        return JOLLY.equalsIgnoreCase(c.getValue()) || TWO.equals(c.getValue());
    }
}
