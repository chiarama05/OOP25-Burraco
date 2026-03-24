package it.unibo.burraco.controller.closure;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.card.Card;

import java.util.List;

public class ClosureValidator {
    
     private ClosureValidator() {
    }

    
    public static ClosureState evaluate(Player player) {

        boolean handEmpty=player.getHand().isEmpty();
        boolean hasPot=player.isInPot();
        boolean hasBurraco=player.getBurracoCount()>= 1;


        if (!handEmpty) {
            return ClosureState.OK;
        }

        // Hand is empty from here on

        // Must take the pot before anything else can happen.
        if (!hasPot) {
            return ClosureState.ZERO_CARDS_NO_POT;
        }


        // if pot taken, hand empty, but no burraco 
        // formed yet → player is stuck and must be warned 
        if (!hasBurraco) {
            return ClosureState.ZERO_CARDS_NO_BURRACO;
        }

        // Pot taken, hand empty, at least one Burraco → can close!
        return ClosureState.CAN_CLOSE;
    }


    public static boolean canCloseByDiscarding(Player player) {
        return player.isInPot() && player.getBurracoCount() >= 1 && player.getHand().size() == 1;
    }

    
    public static ClosureState evaluateAfterDiscard(Player player) {
        if (player.getHand().isEmpty() && player.isInPot()) {
            return player.getBurracoCount()>=1 ? ClosureState.ROUND_WON : ClosureState.CANNOT_CLOSE_NO_BURRACO;
        }
        return ClosureState.OK;
    }


    /*simultes what happens if the player puts down as a new combination
    
    Returns true if the action would leave the player in an unresolvable
     state:
       - the hand would have exactly 1 card left (only option = discard)
       - AND the player has no burraco yet (cannot legally close)
       - AND the pot is already taken (cannot take pot to get more cards)
       - AND the combination being played does NOT itself become a burraco
    
    The special case where the combination itself is >= 7 (instant burraco)
    is allowed even if 1 card remains, because that combination IS the burraco.
    */
    public static boolean wouldGetStuckAfterPutCombo(Player player, List<Card> cardsToPlay, int comboSize) {
        int handAfter = player.getHand().size() - cardsToPlay.size();
        boolean potTaken = player.isInPot();
        boolean hasBurraco = player.getBurracoCount() >= 1;
        boolean newIsBurraco = comboSize >= 7;

        // Se deve ancora prendere il pozzetto, può andare a zero ("al volo")
        if (!potTaken) {
            return false;
        }

        // --- DA QUI IN POI: IL GIOCATORE HA GIÀ IL POZZETTO ---

        // REGOLA: Non puoi mai restare con 0 carte calando una combinazione.
        // Devi averne almeno una per lo scarto finale.
        if (handAfter == 0) {
            return true; // BLOCCA LA MOSSA
        }

        // Se resti con 1 carta:
        if (handAfter == 1) {
            // Puoi farlo solo se hai già un burraco (o lo fai ora con questa calata)
            // così quella carta rimasta sarà lo scarto della vittoria.
            if (hasBurraco || newIsBurraco) {
                return false;
            } else {
                return true; // BLOCCA: resterebbe con 1 carta ma senza poter chiudere
            }
        }

        return false;
    }

    /*
     * Simulazione per ATTACCO (Attach)
     */
    public static boolean wouldGetStuckAfterAttach(Player player, List<Card> cardsToAttach, int currentComboSize) {
        int comboSize = cardsToAttach.size(); 
        int handAfter = player.getHand().size() - comboSize;
        boolean potTaken = player.isInPot();
        boolean hasBurraco = player.getBurracoCount() >= 1;
        boolean attachMakesBurraco = (currentComboSize + comboSize) >= 7;

        if (!potTaken) {
            return false;
        }

        // --- DA QUI IN POI: IL GIOCATORE HA GIÀ IL POZZETTO ---

        // REGOLA: Non puoi mai restare con 0 carte attaccando.
        if (handAfter == 0) {
            return true; // BLOCCA LA MOSSA
        }

        if (handAfter == 1) {
            if (hasBurraco || attachMakesBurraco) {
                return false; 
            } else {
                return true; 
            }
        }

        return false;
    }

}
