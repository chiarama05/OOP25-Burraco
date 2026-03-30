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
public class StraightAttachUtils {

    /**
     * Checks if a list of new cards can be attached to an existing straight.
     * @param straight the current straight on the table
     * @param newCards the cards the player wants to add
     * @return true if the resulting combination is valid or if a wildcard substitution occurs
     */
    public static boolean canAttachToStraight(List<Card> straight, List<Card> newCards) {
        List<Card> potentialStraight = new ArrayList<>(straight);
        potentialStraight.addAll(newCards); 

        if (CombinationValidator.isValidCombination(potentialStraight)) {
            return true;
        }

        // Special case: if only one card is added, check if it can replace an internal wildcard
        if (newCards.size() == 1) {
            return canSubstituteInternalWildcard(straight, newCards.get(0));
        }
        return false;
    }

    /**
     * Overloaded method to check if a single card can be attached to a straight.
     * * @param straight the current straight on the table
     * @param newCard the single card to add
     * @return true if the card can be attached
     */
    public static boolean canAttachToStraight(List<Card> straight, Card newCard) {
        return canAttachToStraight(straight, List.of(newCard));
    }

    /**
     * Checks if a specific card can substitute a wildcard (Jolly/2) currently
     * positioned inside a straight.
     * @param straight the current straight
     * @param newCard the card that might replace the wildcard
     * @return true if the new card matches the required rank and suit to fill the gap
     */
    private static boolean canSubstituteInternalWildcard(List<Card> straight, Card newCard) {
        List<Card> ord = StraightUtils.orderStraight(straight);
        
        for (int i = 0; i < ord.size(); i++) {
            Card current = ord.get(i);
            if (CombinationValidator.isWildcard(current, straight)) {
                if (i > 0 && i < ord.size() - 1) {
                    Card prev = ord.get(i - 1);
                    Card next = ord.get(i + 1);

                    if (!CombinationValidator.isWildcard(prev, straight) && !CombinationValidator.isWildcard(next, straight)) {
                        // The new card must have the same seed as the straight
                        if (!newCard.getSeed().equals(prev.getSeed())) continue;

                        int vPrev = prev.getNumericalValue();
                        int vNew = newCard.getNumericalValue();

                        // Special Case: King (K) filling the gap between Queen (Q) and Ace (A)
                        if (vPrev == 12 && next.getValue().equals("A") && newCard.getValue().equals("K")) {
                            return true;
                        }
                   
                        // Standard Case: The card value is the successor of the previous card
                        if (vNew == vPrev + 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}