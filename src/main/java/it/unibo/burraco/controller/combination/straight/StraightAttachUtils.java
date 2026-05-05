package it.unibo.burraco.controller.combination.straight;

import java.util.ArrayList;
import java.util.List;

import it.unibo.burraco.controller.combination.CombinationValidator;
import it.unibo.burraco.model.card.Card;

/**
 * Utility class for managing attachments and substitutions in straight combinations.
 * It provides logic to verify if new cards can be added to an existing straight,
 * including the special case of replacing an internal wildcard.
 */
public final class StraightAttachUtils {

    private static final int MAX_STRAIGHT_VALUE = 12;
    private final CombinationValidator validator;
    private final StraightUtils straightUtils;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    public StraightAttachUtils() {
        this.validator = new CombinationValidator();
        this.straightUtils = new StraightUtils();
    }

    /**
     * Checks if a list of new cards can be attached to an existing straight.
     *
     * @param straight the current straight on the table
     * @param newCards the cards the player wants to add
     * @return true if the resulting combination is valid or if a wildcard substitution occurs
     */
    public boolean canAttachToStraight(final List<Card> straight, final List<Card> newCards) {
        final List<Card> potentialStraight = new ArrayList<>(straight);
        potentialStraight.addAll(newCards);

        return validator.isValidCombination(potentialStraight)
                || newCards.size() == 1 && canSubstituteInternalWildcard(straight, newCards.get(0));
    }

    /**
     * Checks if a single card can be attached to an existing straight.
     *
     * @param straight the current straight on the table
     * @param newCard  the single card to add
     * @return true if the card can be attached
     */
    public boolean canAttachToStraight(final List<Card> straight, final Card newCard) {
        return canAttachToStraight(straight, List.of(newCard));
    }

    /**
     * Checks if a specific card can substitute a wildcard currently positioned inside a straight.
     *
     * @param straight the current straight
     * @param newCard  the card that might replace the wildcard
     * @return true if the new card matches the required rank and suit to fill the gap
     */
    private boolean canSubstituteInternalWildcard(final List<Card> straight, final Card newCard) {
        final List<Card> ord = straightUtils.orderStraight(straight);

        for (int i = 1; i < ord.size() - 1; i++) {
            final Card current = ord.get(i);
            if (!validator.isWildcard(current, straight)) {
                continue;
            }
            final Card prev = ord.get(i - 1);
            final Card next = ord.get(i + 1);

            if (validator.isWildcard(prev, straight)
                    || validator.isWildcard(next, straight)) {
                continue;
            }
            if (!newCard.getSeed().equals(prev.getSeed())) {
                continue;
            }

            final int vPrev = prev.getNumericalValue();

            if (vPrev == MAX_STRAIGHT_VALUE && "A".equals(next.getValue()) && "K".equals(newCard.getValue())) {
                return true;
            }
            final int vNew = newCard.getNumericalValue();
            if (vNew == vPrev + 1) {
                return true;
            }
        }
        return false;
    }
}
