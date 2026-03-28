package it.unibo.burraco.controller.combination;

import it.unibo.burraco.model.card.*;

import java.util.*;

/**
 * Utility class responsible for validating whether a set of cards 
 * forms a legal game combination (Straight or Set) according to the rules.
 */
public class CombinationValidator {

    /**
     * Validates a combination of cards. 
     * Checks for minimum size, type of combination (Straight/Set), 
     * and the correct usage of wildcards (Jolly and 2).
     * @param cards the list of cards to validate
     * @return true if the combination is valid, false otherwise
     */ 
    public static boolean isValidCombination(List<Card> cards) {
        // A combination must have at least 3 cards
        if (cards == null || cards.size() < 3) return false;

        // Filter cards that are not wildcards
        List<Card> realCards = cards.stream()
            .filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2"))
            .collect(java.util.stream.Collectors.toList());

        // Check if there are duplicate values
        boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(java.util.stream.Collectors.toSet()).size() < realCards.size();

        // Case 1: The combination is a Set
        if (hasDuplicateValues) {
            if (SetUtils.isValidSet(cards)) {
                long wildcardsInSet = cards.stream()
                    .filter(c -> c.getValue().equalsIgnoreCase("Jolly") || c.getValue().equals("2"))
                    .count();
                return wildcardsInSet <= 1;
            }
            return false;
        }

        // Case 2: The combination is a Straight
        if (StraightUtils.isSameSeed(cards)) {
            List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(cards));
            int effectiveWildcards = 0;
            for (int i = 0; i < ordered.size(); i++) {
                Card c = ordered.get(i);
                if (c.getValue().equalsIgnoreCase("Jolly")) {
                    effectiveWildcards++;
                } else if (c.getValue().equals("2")) {
                    // A "2" is an effective wildcard only if it's not in its natural position
                    if (!StraightUtils.isPositionallyNatural(i, ordered)) {
                        effectiveWildcards++;
                    }
                }
            }
            if (effectiveWildcards > 1) return false;
            return StraightUtils.isValidStraight(cards);
        }
 
        // Final check for Sets without duplicate cards
        if (SetUtils.isValidSet(cards)) {
            long wildcardsInSet = cards.stream()
                .filter(c -> c.getValue().equalsIgnoreCase("Jolly") || c.getValue().equals("2"))
                .count();
            return wildcardsInSet <= 1;
        }

        return false;
    }

    /**
     * Determines if a specific card is acting as a wildcard.
     * @param c the card to check
     * @param context the combination of cards the card belongs to
     * @return true if the card is a Joker or a "2" used as a wildcard, false otherwise
     */
    public static boolean isWildcard(Card c, List<Card> context) {
        if (c.getValue().equalsIgnoreCase("Jolly")) return true;
        if (!c.getValue().equals("2")) return false;
        if (c instanceof CardImpl && ((CardImpl) c).isUsedAsWildcard()) return true;
    
        // Analysis for the "2"
        List<Card> realCards = context.stream()
            .filter(r -> !r.getValue().equalsIgnoreCase("Jolly") && !r.getValue().equals("2"))
            .collect(java.util.stream.Collectors.toList());

        boolean hasDuplicateValues = realCards.stream()
            .map(Card::getValue)
            .collect(java.util.stream.Collectors.toSet()).size() < realCards.size();
   
        // In a Set a "2" is always a wildcard
        if (hasDuplicateValues || !StraightUtils.isSameSeed(context)) return true;

        // In a Straight a "2" is a wildcard only if not in its natural sequence
        List<Card> ordered = StraightUtils.orderStraight(new ArrayList<>(context));
        int index = ordered.indexOf(c);
        return !StraightUtils.isPositionallyNatural(index, ordered);
    }

}
