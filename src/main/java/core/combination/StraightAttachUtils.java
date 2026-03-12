package core.combination;

import java.util.List;

import model.card.Card;

public class StraightAttachUtils {

    public static boolean canAttachToStraight(List<Card> straight, Card newCard) {

        List<Card> ord = StraightUtils.orderStraight(straight);

        long jollyCount = ord.stream()
                .filter(c -> CombinationValidator.isWildcard(c, straight))
                .count();

        // -------------------------------------------------
        // Se la nuova è matta
        // -------------------------------------------------
        if (CombinationValidator.isWildcard(newCard, straight)) {

            if (jollyCount >= 1) {

                boolean hasNaturaleDue = ord.stream()
                        .anyMatch(c ->
                                c.getValue().equals("2") &&
                                StraightUtils.isNaturalTwo(c, straight)
                        );

                if (!hasNaturaleDue) return false;
            }

            return true;
        }

        // -------------------------------------------------
        // Trova primo e ultimo NON jolly
        // -------------------------------------------------
        Card first = null;
        Card last = null;

        for (Card c : ord) {
            if (!CombinationValidator.isWildcard(c, straight)) {
                if (first == null) first = c;
                last = c;
            }
        }

        if (first == null) return false;

        // Deve avere stesso seme
        if (!newCard.getSeed().equals(first.getSeed())) return false;

        int vNew = mapVal(newCard);
        int vFirst = mapVal(first);
        int vLast = mapVal(last);

        // -------------------------------------------------
// ATTACCO AI BORDI (LOGICA SIMMETRICA)
// -------------------------------------------------

// --- ATTACCO A SINISTRA (Basso) ---
// Se ho 2-3 e provo ad attaccare A (vNew=1, vFirst=2)
if (vNew == vFirst - 1) return true;

// Se ho Jolly-3-4 (Jolly fa il 2) e attacco A (vNew=1, vFirst=3, jollyCount=1)
if (jollyCount > 0 && vNew == vFirst - 2) return true;


// --- ATTACCO A DESTRA (Alto) ---
// Caso normale: 5-6 -> attacco 7
if (vNew == vLast + 1) return true;

// CASO SPECIALE ASSO ALTO: K (13) -> attacco A
if (vLast == 13 && newCard.getValue().equals("A")) return true;

// Se c'è una matta in fondo: Q(12)-Jolly(13) -> attacco A
if (jollyCount > 0 && vLast == 12 && newCard.getValue().equals("A")) return true;

// Se c'è una matta che fa l'Asso: K(13)-Jolly(14) -> attacco 2
if (jollyCount > 0 && vLast == 13 && newCard.getValue().equals("2")) return true;

        // -------------------------------------------------
        // SOSTITUZIONE MATTA INTERNA
        // -------------------------------------------------

        for (int i = 0; i < ord.size(); i++) {

            Card c = ord.get(i);

            if (CombinationValidator.isWildcard(c, straight)) {

                if (i > 0 && i < ord.size() - 1) {

                    Card c1 = ord.get(i - 1);
                    Card c2 = ord.get(i + 1);

                    if (!CombinationValidator.isWildcard(c1, straight)
                            && !CombinationValidator.isWildcard(c2, straight)) {

                        int v1 = mapVal(c1);
                        int v2 = mapVal(c2);

                        if (v2 - v1 == 2 ||
                            (v1 == 13 && v2 == 2) ||
                            (v1 == 12 && c2.getValue().equals("A"))) {

                            int mattaValue = v1 + 1;

                            if (v1 == 13) mattaValue = 1;
                            if (v1 == 12 && c2.getValue().equals("A")) mattaValue = 13;

                            if (vNew == mattaValue) return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static int mapVal(Card c) {
    if (c.getValue().equals("A")) {
        // Restituiamo 1 di default, ma la logica di attacco 
        // deve saper gestire anche il caso 14 (dopo il K)
        return 1;
    }
    return c.getNumericalValue();
}
}