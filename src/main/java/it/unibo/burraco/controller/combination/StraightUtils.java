package it.unibo.burraco.controller.combination;

import it.unibo.burraco.model.card.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Utility class that provides helper methods to validate and order
 * straight (sequence) combinations in card games.
 *
 * This class handles:
 * - Checking if a sequence has all cards of the same suit
 * - Validating a straight including wildcards (Jolly or usable Twos)
 * - Determining if a "2" is natural in a straight
 * - Mapping card values to numeric values for sequence checking
 * - Ordering a straight including wildcards
 */
public class StraightUtils {
    
    /**
     * Checks if all non-wildcard cards in the list belong to the same seed.
     * A straight must contain cards of the same seed.
     *
     * @param cards the list of cards to check
     * @return true if all non-wildcards have the same seed, false otherwise
     */
    public static boolean isSameSeed(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return false;
        }

        List<Card> pureReal = cards.stream().filter(c -> !c.getValue().equalsIgnoreCase("Jolly") && !c.getValue().equals("2")).collect(Collectors.toList());
        
        if (pureReal.isEmpty()){
            return true;
        } 

        String suit = pureReal.get(0).getSeed();

        return pureReal.stream().allMatch(c -> c.getSeed().equals(suit));
    }


    public static boolean isValidStraight(List<Card> cards) {

    if (cards == null || cards.size() < 3){
        return false;
    }


    if (checkLogic(cards, false)){
        return true;
    } 


    if (checkLogic(cards, true)){
        return true;
    } 

    return false;
}

private static boolean checkLogic(List<Card> cards, boolean forceTwosAsWildcards) {
    List<Card> real = new ArrayList<>();
    int wildcards = 0;

boolean naturalTwoUsed = false;
for (Card c : cards) {
    if (c.getValue().equals("Jolly")) {
        wildcards++;
    } else if (c.getValue().equals("2")) {
        if (!forceTwosAsWildcards && !naturalTwoUsed && isSameSeedAsRest(c, cards)) {
            real.add(c);
            naturalTwoUsed = true;
        } else {
            wildcards++;
        }
    } else {
        real.add(c);
    }
}
    if (wildcards > 1) {
        return false;
    }

    if (real.isEmpty()) {
        return false;
    }


    String referenceSuit = real.get(0).getSeed();
    for (Card c : real) {
        if (!c.getSeed().equals(referenceSuit)) {
            return false;
        }
    }

    
    List<Integer> aceLow = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
    List<Integer> aceHigh = real.stream().map(c -> mapValue(c, false)).sorted().collect(Collectors.toList());

    return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
}

private static boolean isSameSeedAsRest(Card two, List<Card> cards) {
    return cards.stream()
        .filter(c -> !c.getValue().equals("Jolly") && !c.getValue().equals("2"))
        .findFirst()
        .map(firstReal -> firstReal.getSeed().equals(two.getSeed()))
        .orElse(true);
    }


   public static boolean isNaturalTwo(Card two, List<Card> straight) {
    if (!two.getValue().equals("2")) return false;
    
    String suit = two.getSeed();
    // Un 2 è naturale solo se nella lista ci sono l'Asso o il 3 di quel seme
    boolean hasAce = straight.stream().anyMatch(c -> c.getValue().equals("A") && c.getSeed().equals(suit));
    boolean hasThree = straight.stream().anyMatch(c -> c.getValue().equals("3") && c.getSeed().equals(suit));
    
    return hasAce || hasThree;
}


    /**
     * Maps a card to its numeric value.
     * If aceLow is true, Ace = 1; otherwise Ace = 14.
     *
     * @param c the card to map
     * @param aceLow whether Ace should be considered low (1)
     * @return numeric value of the card
     */
    private static int mapValue(Card c, boolean aceLow) {
        if (c.getValue().equals("A")) return aceLow ? 1 : 14;
        return c.getNumericalValue();
    }

    /**
     * Checks if a sorted list of numeric values can become sequential
     * using the given number of wildcards to fill gaps.
     *
     * @param values sorted list of numeric values of cards
     * @param wildcards number of available wildcards to fill missing numbers
     * @return true if the values can form a sequence, false otherwise
     */
    private static boolean canBeSequential(List<Integer> values, long wildcards) {
        if (values.size() < 2) return true;
    

    Set<Integer> set = new HashSet<>(values);
    if (set.size() != values.size()) return false;

    int totalGap = 0;
    for (int i = 0; i < values.size() - 1; i++) {
        int gap = values.get(i + 1) - values.get(i) - 1;
        if (gap < 0) return false;
        totalGap += gap;
    }
    return totalGap <= wildcards;
    }

 public static List<Card> orderStraight(List<Card> sequence) {
    if (sequence == null || sequence.isEmpty()) return new ArrayList<>();

    List<Card> attempt = buildOrdering(sequence, false);
    if (attempt != null) return attempt;

    attempt = buildOrdering(sequence, true);
    if (attempt != null) return attempt;

    return new ArrayList<>(sequence);
}


private static List<Card> buildOrdering(List<Card> sequence, boolean forceTwosAsWild) {
    List<Card> wilds = new ArrayList<>();
    List<Card> real  = new ArrayList<>();

boolean naturalTwoUsed = false;
for (Card c : sequence) {
    if (c.getValue().equalsIgnoreCase("Jolly")) {
        wilds.add(c);
    } else if (c.getValue().equals("2")) {
        if (!forceTwosAsWild && !naturalTwoUsed && isSameSeedAsRest(c, sequence)) {
            real.add(c);
            naturalTwoUsed = true;
        } else {
            wilds.add(c);
        }
    } else {
        real.add(c);
    }
}
    if (wilds.size() > 1 || real.isEmpty()) return null;

    String suit = real.get(0).getSeed();
    if (!real.stream().allMatch(c -> c.getSeed().equals(suit))) return null;

    boolean aceLow = decideIfAceIsLow(real, wilds.size());
    real.sort(Comparator.comparingInt(c -> mapValue(c, aceLow)));


    int totalGap = 0;
    for (int i = 0; i < real.size() - 1; i++) {
        int gap = mapValue(real.get(i + 1), aceLow) - mapValue(real.get(i), aceLow) - 1;
        if (gap < 0) return null;
        totalGap += gap;
    }
    if (totalGap > wilds.size()) return null;

  
    List<Card> wildsCopy = new ArrayList<>(wilds);
    List<Card> result = new ArrayList<>();
    for (int i = 0; i < real.size(); i++) {
        result.add(real.get(i));
        if (i < real.size() - 1) {
            int gap = mapValue(real.get(i + 1), aceLow) - mapValue(real.get(i), aceLow) - 1;
            while (gap > 0 && !wildsCopy.isEmpty()) {
                result.add(wildsCopy.remove(0));
                gap--;
            }
        }
    }
    while (!wildsCopy.isEmpty()) {
        Card w = wildsCopy.remove(0);
        int lastVal  = mapValue(result.get(result.size() - 1), aceLow);
        int firstVal = mapValue(result.get(0), aceLow);
        if      (lastVal < 14)  result.add(w);
        else if (firstVal > 1)  result.add(0, w);
        else                    result.add(w);
    }
    return result;
}


    private static boolean decideIfAceIsLow(List<Card> real, int wildCount) {
        List<Integer> lowVals = real.stream().map(c -> mapValue(c, true)).sorted().collect(Collectors.toList());
        if (canBeSequential(lowVals, wildCount)) {
            if (real.stream().anyMatch(c -> c.getValue().equals("K"))) return false;
            return true;
        }
        return false;
    }


    public static boolean isPositionallyNatural(int index, List<Card> ordered) {
    if (index < 0 || index >= ordered.size()) {
        return false;
    }

    Card c = ordered.get(index);
    if (!c.getValue().equals("2")) return false;

    
    if (index < ordered.size() - 1) {
        Card next = ordered.get(index + 1);
        if (next.getValue().equals("3") && next.getSeed().equals(c.getSeed())) {
            return true;
        }
    }

    if (index > 0 && index < ordered.size() - 1) {
        Card prev = ordered.get(index - 1);
        Card next = ordered.get(index + 1);
        if (prev.getValue().equals("A") && next.getValue().equals("3") && prev.getSeed().equals(c.getSeed())) {
            return true;
        }
    }
    
    if (index > 0) {
        Card prev = ordered.get(index - 1);
        if (prev.getValue().equals("A") && prev.getSeed().equals(c.getSeed())) {
            return true;
        }
    }

    return false;
}
}



