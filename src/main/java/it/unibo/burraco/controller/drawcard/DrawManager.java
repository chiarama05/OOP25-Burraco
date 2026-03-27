package it.unibo.burraco.controller.drawcard;

import java.util.List;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.*;

/**
 * Manages the logic for drawing cards from the deck or the discard pile.
 * It enforces turn-based constraints, such as ensuring a player only draws once per turn.
 */
public class DrawManager {

    /**
     * Flag that tracks whether the current player has already drawn a card
     * during this turn.
     */
    private boolean drawCard = false;

    /**
     * Resets the draw state. 
     */
    public void resetTurn(){
        drawCard = false;
    }

    /**
     * Executes a draw action from the main deck.
     * * @param player the player drawing the card.
     * @param deck the game deck.
     * @return a {@link DrawResult} indicating success or the specific failure reason.
     */
    public DrawResult drawFromDeck(Player player, Deck deck){

        // Prevent drawing if already done this turn
        if (drawCard) {
            return DrawResult.alreadyDrawn();
        }

        Card card = deck.draw();

        // Handle case where the deck is empty
         if (card == null) {
            return DrawResult.emptyDeck();
        }

        player.addCardHand(card);
        // Mark turn as "drawn"
        drawCard = true;

        return DrawResult.success(card);
    }

    /**
     * Executes a draw action from the discard pile, taking all available cards.
     * * @param player the player taking the discards.
     * @param discards the current list of cards in the discard pile.
     * @return a {@link DrawResult} reflecting the outcome of the action.
     */
    public DrawResult drawFromDiscard(Player player, List<Card> discards){
        if (drawCard) {
            return DrawResult.alreadyDrawn();
        }

        if (discards.isEmpty()) {
            return DrawResult.emptyDiscard();
        }

        // Add all cards from the pile to the player's hand
        player.getHand().addAll(discards);
        // Clear the discard pile as the Burraco rules
        discards.clear();
        drawCard = true;

        return DrawResult.successMultiple();
    }

    /**
     * @return true if a draw action has already occurred in the current turn.
     */
    public boolean hasDrawn() {
        return drawCard;
    }
}
