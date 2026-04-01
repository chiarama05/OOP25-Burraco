package it.unibo.burraco.controller.drawcard;

import java.util.List;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.Player;

/**
 * Manages the logic for drawing cards from the deck or the discard pile.
 * It enforces turn-based constraints, such as ensuring a player only draws once per turn.
 */
public class DrawManager {

    private boolean drawCard;

    /**
     * Resets the draw state.
     */
    public void resetTurn() {
        this.drawCard = false;
    }

    /**
     * Executes a draw action from the main deck.
     *
     * @param player the player drawing the card.
     * @param deck the game deck.
     * @return a {@link DrawResult} indicating success or the specific failure reason.
     */
    public DrawResult drawFromDeck(final Player player, final Deck deck) {
        // Prevent drawing if already done this turn
        if (this.drawCard) {
            return DrawResult.alreadyDrawn();
        }

        final Card card = deck.draw();

        if (card == null) {
            return DrawResult.emptyDeck();
        }

        player.addCardHand(card);
        this.drawCard = true;

        return DrawResult.success(card);
    }

    /**
     * Executes a draw action from the discard pile, taking all available cards.
     *
     * @param player the player taking the discards.
     * @param discards the current list of cards in the discard pile.
     * @return a {@link DrawResult} reflecting the outcome of the action.
     */
    public DrawResult drawFromDiscard(final Player player, final List<Card> discards) {
        if (this.drawCard) {
            return DrawResult.alreadyDrawn();
        }

        if (discards.isEmpty()) {
            return DrawResult.emptyDiscard();
        }

        player.getHand().addAll(discards);
        discards.clear();
        this.drawCard = true;

        return DrawResult.successMultiple();
    }

    /**
     * @return true if a draw action has already occurred in the current turn.
     */
    public boolean hasDrawn() {
        return this.drawCard;
    }
}
