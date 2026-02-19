package core.pickcard;
import core.pickcard.DrawResult;

import java.util.List;

import javax.smartcardio.Card;

import carta.Deck;

public class DrawManager {

    private boolean drawCard = false;

    private void resetTurn(){
        drawCard = false;
    }

    public DrawResult drawFromDeck(Player player, Deck deck){

        if (drawCard) {
            return DrawResult.alreadyDrawn();
        }

        Card card = deck.draw();

         if (card == null) {
            return DrawResult.emptyDeck();
        }

        player.aggiungiCartaMano(card);
        drawCard = true;

        return DrawResult.success(card);
    }

    public DrawResult drawFromDiscard(Player player, List<Card> discards){

        if (drawCard) {
            return DrawResult.alreadyDrawn();
        }

        if (discards.isEmpty()) {
            return DrawResult.emptyDiscard();
        }

        player.getHand().addAll(discards);
        discards.clear();
        drawCard = true;

        return DrawResult.successMultiple();
    }
}
