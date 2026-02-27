package core.drawcard;

import java.util.List;

import model.card.Card;
import model.deck.Deck;
import model.player.*;

/**
 * DrawManager handles the logic related to drawing cards during a player's turn.
 */
public class DrawManager {

    /**
     * Flag that tracks whether the current player has already drawn a card
     * during this turn.
     */
    private boolean drawCard = false;

    /**
     * Resets the draw state at the beginning of a new turn.
     */
    public void resetTurn(){
        drawCard = false;
    }

    /**
     * Draws one card from the deck.
     *
     * @param player The current player.
     * @param deck   The game deck.
     * @return DrawResult object describing the outcome.
     */
    public DrawResult drawFromDeck(Player player, Deck deck){

        // Prevent multiple draws in the same turn
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

    /**
     * Draws all cards from the discard pile.
     *
     * @param player   The current player.
     * @param discards The discard pile.
     * @return DrawResult object describing the outcome.
     */
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
