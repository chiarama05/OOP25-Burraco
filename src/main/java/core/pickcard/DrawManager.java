package core.pickcard;
import card.Card;
import player.*;

import java.util.List;

import card.Deck;

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

        player.addCardHand(card);
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
