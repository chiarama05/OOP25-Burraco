package core.combination;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class StraightAttachUtils {

    public static boolean canAttachToStraight(List<Card> straight, Card newCard) {
        List<Card> ord = StraightUtils.orderStraight(straight);

        // 1. CALCOLO DELLE MATTE GIÀ PRESENTI
        // Usiamo CombinationValidator.isWildcard che tiene conto del 2 naturale
        long jollyCount = straight.stream()
                .filter(c -> CombinationValidator.isWildcard(c, straight))
                .count();

        // 2. LOGICA DEL "2" (NATURALE VS MATTA)
        if (newCard.getValue().equals("2")) {
            List<Card> futureStraight = new ArrayList<>(straight);
            futureStraight.add(newCard);

            // Se il 2 che sto per aggiungere SAREBBE NATURALE nella nuova scala
            if (!CombinationValidator.isWildcard(newCard, futureStraight)) {
                // Posso sempre aggiungerlo (il 2 naturale non occupa lo slot matta)
                // A patto che ci sia spazio nella sequenza (gestito sotto dai mapVal)
            } else {
                // Se il 2 che sto aggiungendo è una MATTA
                if (jollyCount >= 1) return false; // Se c'è già un Jolly/2-matta, rifiuta
            }
        } 
        
        // 3. LOGICA DEL JOLLY PURO
        else if (newCard.getValue().equals("Jolly")) {
            if (jollyCount >= 1) return false; // Se c'è già una matta, rifiuta
            return true; // Il Jolly può sempre essere attaccato se non ce ne sono altri
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
// ATTACCO AI BORDI (LOGICA DINAMICA MATTE)
// -------------------------------------------------

// Una matta è "libera" di muoversi se è ad una delle due estremità
boolean hasFreeWildcard = CombinationValidator.isWildcard(ord.get(0), straight) || 
                          CombinationValidator.isWildcard(ord.get(ord.size() - 1), straight);

// --- ATTACCO A SINISTRA (Basso) ---
// Caso normale: 5-6 -> attacco 4
if (vNew == vFirst - 1) return true;

// Caso con salto: 6-7-Jolly -> attacco 4. 
// Se ho una matta libera (anche se è a destra!) e attacco la posizione vFirst - 2
if (hasFreeWildcard && vNew == vFirst - 2) return true;


// --- ATTACCO A DESTRA (Alto) ---
// Caso normale: 7-8 -> attacco 9
if (vNew == vLast + 1) return true;

// Caso con salto: Jolly-4-5 -> attacco 7
// Se ho una matta libera (anche se è a sinistra!) e attacco la posizione vLast + 2
if (hasFreeWildcard && vNew == vLast + 2) return true;


// --- CASI SPECIALI ASSO (Sempre validi se c'è una matta libera o adiacenza) ---
if (newCard.getValue().equals("A")) {
    // Asso basso: ho 2-3 (vFirst=2) oppure Jolly-3-4 (vFirst=3 + matta libera)
    if (vFirst == 2 || (hasFreeWildcard && vFirst == 3)) return true;
    
    // Asso alto: ho K(13) oppure Q(12) + matta libera
    if (vLast == 13 || (hasFreeWildcard && vLast == 12)) return true;
}

// Caso K con matta libera: Jolly-Q-K -> attacco 2 (Asso diventa matta o aggancio)
if (hasFreeWildcard && vLast == 13 && newCard.getValue().equals("2")) return true;
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