package core.combination;

import model.card.*;
import java.util.*;
import java.util.stream.Collectors;

public class StraightUtils {
    
    public static boolean isStraight(List<Card> cards) {
        List<Card> real = cards.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, cards))
                .collect(Collectors.toList()); 

        if (real.isEmpty()) return false;

        String suit = real.get(0).getSeed();
        return real.stream().allMatch(c -> c.getSeed().equals(suit));
    }

    public static boolean isValidStraight(List<Card> cards) {
        List<Card> real = cards.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, cards))
                .collect(Collectors.toList());

        if (real.isEmpty()) return false;

        List<Integer> aceLow = real.stream()
                .map(c -> mapValue(c, true))
                .sorted()
                .collect(Collectors.toList());

        List<Integer> aceHigh = real.stream()
                .map(c -> mapValue(c, false))
                .sorted()
                .collect(Collectors.toList());

        long wildcards = cards.stream()
                .filter(c -> CombinationValidator.isWildcard(c, cards))
                .count();

        return canBeSequential(aceLow, wildcards) || canBeSequential(aceHigh, wildcards);
    }

    public static boolean isNaturalTwo(Card two, List<Card> straight) {
        if (!two.getValue().equals("2")) return false;

        List<Card> real = straight.stream()
                .filter(c -> !c.getValue().equals("2") && !c.getValue().equals("Jolly"))
                .collect(Collectors.toList());

        if (real.isEmpty()) return false;

        String suit = real.get(0).getSeed();
        if (!two.getSeed().equals(suit)) return false;

        boolean hasAce = real.stream().anyMatch(c -> c.getValue().equals("A"));
        boolean hasThree = real.stream().anyMatch(c -> c.getValue().equals("3"));

        return hasAce && hasThree;
    }

    private static int mapValue(Card c, boolean aceLow) {
        if (c.getValue().equals("A")) return aceLow ? 1 : 14;
        return c.getNumericalValue();
    }

    private static boolean canBeSequential(List<Integer> values, long wildcards) {

        if (values.size() < 2) return true;

        if (new HashSet<>(values).size() != values.size()) return false;

        Collections.sort(values);

        int neededWildcards = 0;

        for (int i = 0; i < values.size() - 1; i++) {
            int gap = values.get(i + 1) - values.get(i) - 1;
            if (gap < 0) return false;

            neededWildcards += gap;
            if (neededWildcards > wildcards) return false;
        }

        return true;
    }

    public static List<Card> orderStraight(List<Card> sequence) {

        List<Card> real = sequence.stream()
                .filter(c -> !CombinationValidator.isWildcard(c, sequence))
                .collect(Collectors.toList());

        List<Card> wild = sequence.stream()
                .filter(c -> CombinationValidator.isWildcard(c, sequence))
                .collect(Collectors.toList());

        if (real.isEmpty()) return new ArrayList<>(sequence);

        List<Integer> aceLow = real.stream().map(c -> mapValue(c, true)).collect(Collectors.toList());
        List<Integer> aceHigh = real.stream().map(c -> mapValue(c, false)).collect(Collectors.toList());

        boolean useAceLow = canBeSequential(aceLow, wild.size());

        List<Integer> usedValues = useAceLow ? aceLow : aceHigh;

        Map<Integer, Card> map = new HashMap<>();
        for (Card c : real) {
            map.put(mapValue(c, useAceLow), c);
        }

        Collections.sort(usedValues);

        List<Card> result = new ArrayList<>();
        int wildIndex = 0;

        for (int i = 0; i < usedValues.size() - 1; i++) {

            int v1 = usedValues.get(i);
            int v2 = usedValues.get(i + 1);

            result.add(map.get(v1));

            int gap = v2 - v1 - 1;

            while (gap-- > 0 && wildIndex < wild.size()) {
                result.add(wild.get(wildIndex++));
            }
        }

        result.add(map.get(usedValues.get(usedValues.size() - 1)));

        while (wildIndex < wild.size()) {
            result.add(wild.get(wildIndex++));
        }

        return result;
    }
}



