package core.combination;

import java.util.List;

import model.card.Card;

public class StraightAttachUtils {

    public static boolean canAttachToStraight(List<Card> straight, Card newCard) {

        List<Card> ordered = StraightUtils.orderStraight(straight);

        long wildcardCount = ordered.stream()
                .filter(c -> CombinationValidator.isWildcard(c, straight))
                .count();

        // -------------------------------------------------
        // 1️⃣ Se la nuova carta è una matta
        // -------------------------------------------------
        if (CombinationValidator.isWildcard(newCard, straight)) {

            if (wildcardCount >= 1) {

                boolean hasNaturalTwo = ordered.stream()
                        .anyMatch(c ->
                                c.getValue().equals("2") &&
                                StraightUtils.isNaturalTwo(c, straight)
                        );

                if (!hasNaturalTwo) {
                    return false;
                }
            }

            return true;
        }

        // -------------------------------------------------
        // 2️⃣ Trova prima e ultima carta reale
        // -------------------------------------------------
        Card first = null;
        Card last = null;

        for (Card c : ordered) {
            if (!CombinationValidator.isWildcard(c, straight)) {
                if (first == null) first = c;
                last = c;
            }
        }

        if (first == null) return false;

        if (!newCard.getSeed().equals(first.getSeed())) {
            return false;
        }

        int vNew = mapValue(newCard);
        int vFirst = mapValue(first);
        int vLast = mapValue(last);

        // -------------------------------------------------
        // 3️⃣ Attacco ai bordi
        // -------------------------------------------------

        // Sinistra
        if (vNew == vFirst - 1) return true;
        if (wildcardCount > 0 && vNew == vFirst - 2) return true;

        // Destra
        if (vNew == vLast + 1) return true;

        // K → A
        if (vLast == 13 && newCard.getValue().equals("A")) return true;

        if (wildcardCount > 0) {
            if (vLast == 12 && newCard.getValue().equals("A")) return true;
            if (vLast == 13 && newCard.getValue().equals("2")) return true;
        }

        // -------------------------------------------------
        // 4️⃣ Sostituzione matta interna
        // -------------------------------------------------
        for (int i = 0; i < ordered.size(); i++) {

            Card c = ordered.get(i);

            if (CombinationValidator.isWildcard(c, straight)) {

                if (i > 0 && i < ordered.size() - 1) {

                    Card c1 = ordered.get(i - 1);
                    Card c2 = ordered.get(i + 1);

                    if (!CombinationValidator.isWildcard(c1, straight)
                            && !CombinationValidator.isWildcard(c2, straight)) {

                        int v1 = mapValue(c1);
                        int v2 = mapValue(c2);

                        if (v2 - v1 == 2) {
                            if (vNew == v1 + 1) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private static int mapValue(Card c) {
        if (c.getValue().equals("A")) return 1;
        return c.getNumericalValue();
    }
}
